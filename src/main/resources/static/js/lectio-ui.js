(() => {
    'use strict';
    const reducedMotion = window.matchMedia('(prefers-reduced-motion: reduce)');
    const reveal = element => element.classList.remove('motion-pending');
    document.querySelectorAll('.motion-pending').forEach(reveal);

    const search = document.querySelector('.toolbar .search input[type="search"]');
    if (search) {
        const books = [...document.querySelectorAll('.books > .book')];
        const count = document.querySelector('.toolbar .count');
        const normalize = text => text.normalize('NFD').replace(/[\u0300-\u036f]/g, '').toLowerCase();
        const filter = () => {
            const term = normalize(search.value.trim());
            let visible = 0;
            books.forEach(book => {
                book.hidden = !normalize(book.textContent).includes(term);
                if (!book.hidden) {
                    reveal(book);
                    visible++;
                }
            });
            if (count) count.textContent = visible ? `${visible} de ${books.length} títulos` : 'No encontramos títulos para esa búsqueda.';
        };
        count?.setAttribute('aria-live', 'polite');
        search.addEventListener('input', filter);
        search.form?.addEventListener('submit', event => { event.preventDefault(); filter(); });
    }

    const toggle = document.getElementById('navToggle');
    const links = document.getElementById('navLinks');
    if (toggle && links) {
        toggle.setAttribute('aria-controls', 'navLinks');
        const drawerVisible = () => getComputedStyle(toggle).display !== 'none';
        const isOpen = () => links.classList.contains('open') || links.classList.contains('active');
        const sync = () => {
            const open = isOpen();
            toggle.setAttribute('aria-expanded', String(open));
            links.inert = drawerVisible() && !open;
        };
        const close = () => {
            links.classList.remove('open', 'active');
            document.querySelectorAll('.nav-overlay').forEach(overlay => overlay.classList.remove('active', 'open'));
            const icon = toggle.querySelector('i');
            if (icon) icon.className = 'fas fa-bars';
            document.body.style.overflow = '';
            sync();
        };
        sync();
        new MutationObserver(sync).observe(links, { attributes: true, attributeFilter: ['class'] });
        document.addEventListener('keydown', event => {
            if (event.key === 'Escape' && isOpen()) {
                close();
                toggle.focus();
            }
        });
        window.addEventListener('resize', () => {
            if (!drawerVisible()) close();
            else sync();
        });
    }

    document.querySelectorAll('.hero video').forEach(video => {
        const update = () => {
            if (reducedMotion.matches || document.hidden) {
                if (!video.paused) video.pause();
            } else if (video.paused) { video.play().catch(() => {}); }
        };
        // Layout changes during startup must not pause an already playing hero.
        video.addEventListener('canplay', update);
        document.addEventListener('visibilitychange', update);
        reducedMotion.addEventListener('change', update);
        update();
    });
})();
