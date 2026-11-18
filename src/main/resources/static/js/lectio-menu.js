(() => {
    const adminPages = ['admin', 'dashboard', 'modeloPredictivo', 'UsuariosAdmin', 'addBook', 'addLibroF', 'reservaAdmin', 'devolucion', 'multasAdmin', 'resenasAdmin', 'NOadminConfig', 'addMulta', 'editarReserva', 'eliminarReserva'];
    const admin = adminPages.includes(document.body.dataset.page);
    let trigger = document.getElementById('experienceToggle') || document.getElementById('navToggle');
    const source = document.getElementById('experienceLinks') || document.getElementById('navLinks');
    if (!trigger && admin) {
        const header = document.querySelector('body > header');
        if (!header) return;
        header.classList.add('has-mobile-menu');
        trigger = document.createElement('button');
        trigger.type = 'button';
        header.append(trigger);
    }
    if (!trigger || (!source && !admin)) return;
    const media = matchMedia('(max-width: 1280px)');
    const reduce = matchMedia('(prefers-reduced-motion: reduce)');
    trigger.classList.add('lectio-menu-trigger');
    trigger.setAttribute('aria-label', 'Abrir menú');
    trigger.setAttribute('aria-haspopup', 'dialog');
    trigger.setAttribute('aria-controls', 'lectioMobileMenu');
    trigger.setAttribute('aria-expanded', 'false');
    // Existing page handlers keep their icon node; the new decoration is CSS-only.
    const decoration = document.createElement('span');
    decoration.className = 'menu-strokes';
    decoration.setAttribute('aria-hidden', 'true');
    decoration.innerHTML = '<span></span><span></span><span></span>';
    trigger.append(decoration);
    const dialog = document.createElement('dialog');
    dialog.id = 'lectioMobileMenu';
    dialog.className = 'lectio-mobile-menu';
    dialog.setAttribute('aria-labelledby', 'mobileMenuTitle');
    dialog.innerHTML = '<div class="mobile-menu-heading"><div><span class="mobile-menu-eyebrow"></span><h2 id="mobileMenuTitle">¿A dónde vamos?</h2></div><button class="mobile-menu-close" type="button" aria-label="Cerrar menú" autofocus>×</button></div><nav class="mobile-menu-options" aria-label="Navegación móvil"></nav><div class="mobile-menu-foot"><img src="/img/lectio-logo.svg" width="40" height="40" alt=""><span>LECTIO<small></small></span></div>';
    dialog.querySelector('.mobile-menu-eyebrow').textContent = admin ? 'Tu espacio de gestión' : 'Tu biblioteca, contigo';
    dialog.querySelector('.mobile-menu-foot small').textContent = admin ? 'Administración de la biblioteca' : 'Una página. Un nuevo comienzo.';
    const options = dialog.querySelector('nav');
    const items = admin ? [
        ['/api/admin', 'Panel de administración'], ['/api/dashboard', 'Estadísticas'], ['/api/modeloPredictivo', 'Modelo predictivo'],
        ['/api/usuariosAdmin', 'Usuarios'], ['/api/addBook', 'Libros virtuales'], ['/api/addLibroF', 'Libros físicos'],
        ['/api/reservaAdmin', 'Reservas'], ['/api/devolucion', 'Devoluciones'], ['/api/multasAdmin', 'Multas'], ['/api/resenasAdmin', 'Reseñas']
    ] : [...source.querySelectorAll('a[href]')].map(link => [link.getAttribute('href'), link.textContent.replace(/[✦🏆]/gu, '').trim()]);
    items.forEach(([href, label]) => {
        const link = document.createElement('a');
        link.href = href;
        const icon = document.createElement('span');
        icon.className = 'mobile-menu-icon';
        icon.setAttribute('aria-hidden', 'true');
        icon.innerHTML = '<svg viewBox="0 0 24 24"><path d="M4 5c4-1 6 0 8 2 2-2 4-3 8-2v14c-4-1-6 0-8 2-2-2-4-3-8-2zM12 7v14"/></svg>';
        const text = document.createElement('span');
        text.textContent = label;
        link.append(icon, text);
        const target = new URL(href, location.href);
        if (target.pathname === location.pathname && (!target.hash || target.hash === location.hash)) link.setAttribute('aria-current', 'page');
        link.style.setProperty('--menu-delay', `${Math.min(options.children.length, 4) * 35}ms`);
        link.addEventListener('click', event => {
            if (event.button !== 0 || event.ctrlKey || event.metaKey || event.shiftKey || event.altKey) return;
            event.preventDefault();
            if (dialog.classList.contains('is-closing')) return;
            closeMenu().then(() => location.assign(link.href));
        });
        options.append(link);
    });
    document.body.append(dialog);
    source?.classList.add('uses-shared-mobile-menu');
    let previousOverflow = '';
    let closing = null;
    let finishClose = null;
    function closeMenu() {
        if (closing) return closing;
        if (!dialog.open) return Promise.resolve();
        if (reduce.matches) { dialog.close(); return Promise.resolve(); }
        dialog.classList.add('is-closing');
        closing = new Promise(resolve => {
            let timer;
            const finish = () => {
                clearTimeout(timer);
                dialog.removeEventListener('animationend', onEnd);
                finishClose = null;
                dialog.close();
                resolve();
            };
            const onEnd = event => {
                if (event.target === dialog && event.animationName === 'lectio-menu-out') finish();
            };
            finishClose = finish;
            dialog.addEventListener('animationend', onEnd);
            // Also close when animations are suppressed by browser preferences/styles.
            timer = setTimeout(finish, 260);
        });
        return closing;
    }
    trigger.addEventListener('click', event => {
        if (!media.matches) return;
        event.preventDefault();
        event.stopImmediatePropagation();
        if (dialog.open) { closeMenu(); return; }
        closing = null;
        dialog.classList.remove('is-closing');
        previousOverflow = document.body.style.overflow;
        document.body.style.overflow = 'hidden';
        trigger.setAttribute('aria-expanded', 'true');
        dialog.showModal();
    }, true);
    dialog.querySelector('.mobile-menu-close').addEventListener('click', closeMenu);
    dialog.addEventListener('cancel', event => { event.preventDefault(); closeMenu(); });
    dialog.addEventListener('click', event => {
        const rect = dialog.getBoundingClientRect();
        if (event.target === dialog && (event.clientX < rect.left || event.clientX > rect.right || event.clientY < rect.top || event.clientY > rect.bottom)) closeMenu();
    });
    dialog.addEventListener('close', () => {
        document.body.style.overflow = previousOverflow;
        trigger.setAttribute('aria-expanded', 'false');
        trigger.focus({ preventScroll: true });
        closing = null;
        dialog.classList.remove('is-closing');
    });
    media.addEventListener('change', () => { if (finishClose) finishClose(); else if (dialog.open) dialog.close(); });
    reduce.addEventListener('change', () => { if (reduce.matches && finishClose) finishClose(); });
})();
