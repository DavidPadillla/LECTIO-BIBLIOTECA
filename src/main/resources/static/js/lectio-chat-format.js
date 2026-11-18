// Escape all source HTML before adding a small, controlled set of formatting tags.
window.lectioMarkdown = text => {
    const escape = value => String(value).replace(/[&<>"']/g, c => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));
    const inline = value => escape(value).replace(/\*\*([^*]+)\*\*/g, '<strong>$1</strong>').replace(/`([^`]+)`/g, '<code>$1</code>').replace(/\*([^*\n]+)\*/g, '<em>$1</em>');
    let list = false;
    const output = [];
    for (const line of String(text).replace(/\r/g, '').split('\n')) {
        const item = line.match(/^\s*(?:[-*]|\d+\.)\s+(.+)$/);
        if (item) { if (!list) output.push('<ul>'); list = true; output.push('<li>' + inline(item[1]) + '</li>'); }
        else { if (list) output.push('</ul>'); list = false; if (line.trim()) output.push('<p>' + inline(line.replace(/^#{1,6}\s+/, '')) + '</p>'); }
    }
    if (list) output.push('</ul>');
    return output.join('');
};

window.lectioChatRetry = (body, retry) => {
    const message = body.lastElementChild;
    const bubble = message?.querySelector('.chat-bubble-msg');
    if (!bubble) return;
    const button = document.createElement('button');
    button.type = 'button'; button.className = 'chat-retry'; button.textContent = 'Reintentar';
    button.addEventListener('click', () => {
        const send = document.getElementById('chatSend');
        if (send?.disabled) return;
        message.remove(); retry();
    });
    bubble.append(button);
};
