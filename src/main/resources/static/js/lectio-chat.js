(() => {
    'use strict';
    if (!document.getElementById('chatWindow')) return;

    const chatBubble  = document.getElementById('chatBubble');
    const chatWindow  = document.getElementById('chatWindow');
    const chatClose   = document.getElementById('chatClose');
    const chatClear   = document.getElementById('chatClear');
    const chatInput   = document.getElementById('chatInput');
    const chatSend    = document.getElementById('chatSend');
    const chatBody    = document.getElementById('chatBody');
    const chatBadge   = chatBubble?.querySelector('.chat-badge');

    let isWaiting = false;
    let currentEvtSource = null;

    function getTime() {
        const n = new Date();
        return `${n.getHours().toString().padStart(2,'0')}:${n.getMinutes().toString().padStart(2,'0')}`;
    }

    function escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }

    function renderMarkdown(text) { return window.lectioMarkdown(text); }

    function decodeBase64(str) {
        try {
            return decodeURIComponent(
                atob(str).split('').map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)).join('')
            );
        } catch (e) {
            return str;
        }
    }

    function addUserMessage(text) {
        const div = document.createElement('div');
        div.className = 'chat-message user';
        div.innerHTML = `
            <div class="chat-bubble-msg">
                <div class="md-content">${escapeHtml(text)}</div>
                <span class="chat-time">${getTime()}</span>
            </div>
            <div class="chat-avatar"><i class="fas fa-user"></i></div>
        `;
        chatBody.appendChild(div);
        chatBody.scrollTop = chatBody.scrollHeight;
    }

    function addBotMessage(text, isError = false) {
        const div = document.createElement('div');
        div.className = 'chat-message';
        div.innerHTML = `
            <div class="chat-avatar"><i class="fas fa-robot"></i></div>
            <div class="chat-bubble-msg${isError ? ' chat-error' : ''}">
                <div class="rag-badge">Lectio</div>
                <div class="md-content">${isError ? escapeHtml(text) : renderMarkdown(text)}</div>
                <span class="chat-time">${getTime()}</span>
            </div>
        `;
        chatBody.appendChild(div);
        chatBody.scrollTop = chatBody.scrollHeight;
        return div;
    }

    function showTyping() {
        if (document.getElementById('typingIndicator')) return;
        const div = document.createElement('div');
        div.className = 'chat-message';
        div.id = 'typingIndicator';
        div.innerHTML = `
            <div class="chat-avatar"><i class="fas fa-robot"></i></div>
            <div class="typing-indicator">
                <span></span><span></span><span></span>
            </div>
        `;
        chatBody.appendChild(div);
        chatBody.scrollTop = chatBody.scrollHeight;
    }

    function hideTyping() {
        const el = document.getElementById('typingIndicator');
        if (el) el.remove();
    }

    function setWaiting(val) {
        isWaiting = val;
        chatSend.disabled = val;
        if (val) {
            chatInput.disabled = true;
        } else {
            chatInput.disabled = false;
            chatInput.focus();
        }
    }

    function closeChat() {
        if (currentEvtSource) {
            currentEvtSource.close();
            currentEvtSource = null;
        }
        setWaiting(false);
        hideTyping();
        chatWindow.classList.remove('active');
        chatBubble.focus();
        if (chatBadge) chatBadge.style.display = 'flex';
    }

    function clearChat() {
        if (currentEvtSource) {
            currentEvtSource.close();
            currentEvtSource = null;
        }
        setWaiting(false);

        fetch('/api/chat/limpiar', {
            method: 'POST',
            credentials: 'include'
        })
            .then(response => response.json())
            .catch(error => {
                console.error('Error limpiando historial:', error);
            });

        chatBody.innerHTML = '<div class="chat-date-sep">Hoy</div>';
        addBotMessage('Chat limpiado. ¿En qué te puedo ayudar?');
    }

    function sendMessageToRAG(message) {
        if (isWaiting) return;
        setWaiting(true);
        showTyping();

        if (currentEvtSource) {
            currentEvtSource.close();
            currentEvtSource = null;
        }

        const url = `/api/chat/rag?pregunta=${encodeURIComponent(message)}`;
        const evtSource = new EventSource(url);
        currentEvtSource = evtSource;

        let streamingDiv = null;
        let streamingContent = null;
        let fullResponse = '';

        evtSource.onmessage = function (event) {
            const data = event.data;

            if (data === '[DONE]') {
                hideTyping();
                evtSource.close();
                currentEvtSource = null;
                if (streamingContent && fullResponse) {
                    streamingContent.innerHTML = renderMarkdown(fullResponse);
                    chatBody.scrollTop = chatBody.scrollHeight;
                }
                setWaiting(false);
                return;
            }

            if (data === '[ERROR]') {
                evtSource.close();
                currentEvtSource = null;
                hideTyping();
                if (!fullResponse) {
                    addBotMessage('No pudimos completar la respuesta. Intenta de nuevo.', true);
                }
                setWaiting(false);
                return;
            }

            const chunk = decodeBase64(data);

            if (!streamingDiv) {
                hideTyping();
                const msgDiv = document.createElement('div');
                msgDiv.className = 'chat-message';
                msgDiv.innerHTML = `
                    <div class="chat-avatar"><i class="fas fa-robot"></i></div>
                    <div class="chat-bubble-msg">
                        <div class="rag-badge">Lectio</div>
                        <div class="md-content" id="streamingContent_${Date.now()}"></div>
                        <span class="chat-time">${getTime()}</span>
                    </div>
                `;
                chatBody.appendChild(msgDiv);
                streamingDiv = msgDiv;
                streamingContent = msgDiv.querySelector('.md-content');
            }

            fullResponse += chunk;
            if (streamingContent) {
                streamingContent.innerHTML = renderMarkdown(fullResponse);
                chatBody.scrollTop = chatBody.scrollHeight;
            }
        };

        evtSource.onerror = function () {
            evtSource.close();
            currentEvtSource = null;
            hideTyping();
            if (fullResponse && streamingContent) {
                streamingContent.innerHTML = renderMarkdown(fullResponse);
            } else if (!fullResponse) {
                addBotMessage('No se pudo conectar. Puedes reintentar esta pregunta.', true); window.lectioChatRetry(chatBody, () => sendMessageToRAG(message));
            }
            setWaiting(false);
        };
    }

    function sendMessage() {
        const msg = chatInput.value.trim();
        if (!msg || isWaiting) return;
        addUserMessage(msg);
        chatInput.value = '';
        sendMessageToRAG(msg);
    }

    chatBubble?.addEventListener('click', () => {
        chatWindow.classList.add('active');
        if (chatBadge) chatBadge.style.display = 'none';
        chatInput.focus();
    });

    chatClose?.addEventListener('click', closeChat);
    chatClear?.addEventListener('click', clearChat);
    chatSend.addEventListener('click', sendMessage);
    chatInput.addEventListener('keypress', e => { if (e.key === 'Enter') sendMessage(); });
    chatWindow.addEventListener('click', e => e.stopPropagation());

    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && chatWindow.classList.contains('active')) {
            closeChat();
        }
    });

    document.querySelectorAll('.suggestion-chip').forEach(chip => {
        chip.addEventListener('click', () => {
            const text = chip.getAttribute('data-suggestion');
            if (text && !isWaiting) {
                chatInput.value = text;
                sendMessage();
            }
        });
    });

    
})();
