(() => {
    'use strict';
    // Only the new shared shell owns this button. Existing page handlers stay intact.
    const toggle = document.getElementById('experienceToggle');
    const links = document.getElementById('experienceLinks');
    if (toggle && links) {
        const mobile = matchMedia('(max-width: 1280px)');
        const setOpen = open => {
            links.classList.toggle('is-open', open);
            toggle.setAttribute('aria-expanded', String(open));
            toggle.setAttribute('aria-label', open ? 'Cerrar menú' : 'Abrir menú');
            links.inert = mobile.matches && !open;
        };
        toggle.addEventListener('click', () => setOpen(!links.classList.contains('is-open')));
        links.addEventListener('click', event => { if (event.target.closest('a')) setOpen(false); });
        document.addEventListener('keydown', event => {
            if (event.key === 'Escape' && links.classList.contains('is-open')) { setOpen(false); toggle.focus(); }
        });
        document.addEventListener('click', event => {
            if (!event.target.closest('.experience-nav')) setOpen(false);
        });
        mobile.addEventListener('change', () => setOpen(false));
        setOpen(false);
    }

    // Keep the POST logout action, with one accessible confirmation for shell forms.
    document.querySelectorAll('.experience-account form:not([hidden])').forEach(form => {
        if (!form.action.endsWith('/api/logout') || form.id === 'logoutForm') return;
        let confirmed = false;
        form.addEventListener('submit', event => {
            if (confirmed) return;
            event.preventDefault();
            const dialog = document.createElement('dialog');
            dialog.className = 'lectio-session-dialog';
            dialog.setAttribute('aria-labelledby', 'session-dialog-title');
            dialog.innerHTML = '<h2 id="session-dialog-title">¿Cerrar sesión?</h2><p>Tu próxima lectura te espera cuando vuelvas.</p><div class="session-dialog-actions"><button type="button" data-cancel autofocus>Seguir leyendo</button><button type="button" data-confirm>Cerrar sesión</button></div>';
            if (document.body.classList.contains('admin-design')) {
                dialog.querySelector('p').textContent = 'Podrás volver a la administración cuando inicies sesión de nuevo.';
                dialog.querySelector('[data-cancel]').textContent = 'Continuar aquí';
            }
            document.body.append(dialog);
            const trigger = event.submitter;
            dialog.addEventListener('close', () => { dialog.remove(); trigger?.focus(); });
            dialog.querySelector('[data-cancel]').addEventListener('click', () => dialog.close());
            dialog.querySelector('[data-confirm]').addEventListener('click', () => {
                confirmed = true;
                dialog.close();
                form.requestSubmit();
            });
            dialog.addEventListener('click', event => {
                const bounds = dialog.getBoundingClientRect();
                if (event.target === dialog && (event.clientX < bounds.left || event.clientX > bounds.right || event.clientY < bounds.top || event.clientY > bounds.bottom)) dialog.close();
            });
            dialog.showModal();
        });
    });

    const chapterDialog = document.querySelector('body.adventure #modal');
    if (chapterDialog) {
        let previousFocus;
        let previousOverflow = '';
        let open = false;
        new MutationObserver(() => {
            const visible = chapterDialog.style.display === 'flex';
            if (visible === open) return;
            open = visible;
            if (open) {
                previousFocus = document.activeElement;
                previousOverflow = document.body.style.overflow;
                document.body.style.overflow = 'hidden';
                chapterDialog.querySelector('.modal-content').scrollTop = 0;
                chapterDialog.querySelector('.chapter-close')?.focus({ preventScroll: true });
            } else {
                document.body.style.overflow = previousOverflow;
                if (previousFocus?.isConnected) previousFocus.focus({ preventScroll: true });
            }
        }).observe(chapterDialog, { attributes: true, attributeFilter: ['style'] });
        chapterDialog.addEventListener('keydown', event => {
            if (event.key === 'Escape') { event.preventDefault(); chapterDialog.querySelector('.chapter-close')?.click(); }
            if (event.key === 'Tab') {
                const buttons = [...chapterDialog.querySelectorAll('button:not([disabled])')];
                const first = buttons[0], last = buttons[buttons.length - 1];
                if (event.shiftKey && document.activeElement === first) { event.preventDefault(); last.focus(); }
                else if (!event.shiftKey && document.activeElement === last) { event.preventDefault(); first.focus(); }
            }
        });
    }

    const chatTrigger = document.getElementById('chatBubble');
    const chatWindow = document.getElementById('chatWindow');
    if (chatTrigger && chatWindow) {
        chatTrigger.setAttribute('role', 'button');
        chatTrigger.tabIndex = 0;
        chatTrigger.setAttribute('aria-label', 'Habla con Lio, tu asistente de Lectio');
        chatTrigger.setAttribute('aria-controls', 'chatWindow');
        const syncChat = () => {
            const open = chatWindow.classList.contains('active');
            chatTrigger.setAttribute('aria-expanded', String(open));
            chatTrigger.classList.toggle('chat-is-open', open);
        };
        new MutationObserver(syncChat).observe(chatWindow, { attributes: true, attributeFilter: ['class'] });
        syncChat();
        chatTrigger.addEventListener('keydown', event => {
            if (event.key === 'Enter' || event.key === ' ') { event.preventDefault(); chatTrigger.click(); }
        });
    }
    const reduce = matchMedia('(prefers-reduced-motion: reduce)');
    if (reduce.matches || !('IntersectionObserver' in window)) return;
    const played = new WeakSet();
    const running = new Set();
    const selectors = '.reveal, .stagger > *, .journey-world, .journey-art, .journey-section-head, .contenido > *, .hero-copy > *, .hero-card, .page-hero .wrap > *, .hero-opt .hero-inner > *, .hero-res .hero-inner > *, .page-content > *, .pensador, .capitulo-nodo, .section-header, .form-card, .page-header, .book-card, .libro-item, .help-card, .admin-card, .stat-card, .libro';
    const observer = new IntersectionObserver(entries => {
        entries.forEach(entry => {
            if (!entry.isIntersecting) return;
            observer.unobserve(entry.target);
            if (played.has(entry.target) || reduce.matches) return;
            played.add(entry.target);
            const siblings = [...entry.target.parentElement.children];
            const delay = Math.min(siblings.indexOf(entry.target) % 4, 3) * 65;
            if (!entry.target.animate) return;
            const frames = entry.target.matches('.capitulo-nodo') ? [
                { opacity: 0, scale: '.9' }, { opacity: 1, scale: '1' }
            ] : [
                { opacity: 0, translate: '0 28px' },
                { opacity: 1, translate: '0 0' }
            ];
            const animation = entry.target.animate(frames, { duration: 720, delay, easing: 'cubic-bezier(.16,1,.3,1)', fill: 'backwards' });
            running.add(animation);
            animation.finished.then(() => running.delete(animation)).catch(() => running.delete(animation));
        });
    }, { threshold: 0, rootMargin: '0px 0px -24px 0px' });
    const observe = root => {
        const watch = element => {
            if (played.has(element)) return;
            element.classList.add('lectio-entrance');
            observer.observe(element);
        };
        if (root.matches?.(selectors)) watch(root);
        root.querySelectorAll?.(selectors).forEach(watch);
    };
    observe(document);
    // Chapters and progress cards are created by the existing reading scripts.
    document.querySelectorAll('#capitulosList, #stats-cards, #lista-libros').forEach(container => {
        new MutationObserver(records => records.forEach(record => record.addedNodes.forEach(node => {
            if (node.nodeType === 1) observe(node);
        }))).observe(container, { childList: true });
    });
    reduce.addEventListener('change', () => {
        if (reduce.matches) { observer.disconnect(); running.forEach(animation => animation.cancel()); running.clear(); }
    });
})();
