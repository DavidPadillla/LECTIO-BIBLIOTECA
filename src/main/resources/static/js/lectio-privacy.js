(() => {
    const key = 'lectio-privacy-v1';
    let choice = null;
    try { const saved = JSON.parse(localStorage.getItem(key)); if (saved?.version === 1) choice = saved; } catch (_) {}
    const banner = document.createElement('section');
    banner.className = 'cookie-banner';
    banner.setAttribute('aria-label', 'Preferencias de privacidad');
    banner.innerHTML = '<div><h2>Tu privacidad, tu elección</h2><p>Usamos cookies de sesión. Elige si permites fuentes y contenido externo.</p><a href="/api/privacidad">Política de privacidad</a></div><div class="cookie-actions"><button type="button" data-all>Aceptar todo</button><button type="button" data-reject>Rechazar lo no esencial</button><button type="button" data-manage>Administrar preferencias</button></div>';
    const reopen = document.createElement('button');
    reopen.type = 'button'; reopen.className = 'privacy-manage-inline'; reopen.textContent = 'Cambiar preferencias de privacidad';
    const dialog = document.createElement('dialog');
    dialog.className = 'cookie-dialog';
    dialog.setAttribute('aria-labelledby', 'cookieTitle');
    dialog.innerHTML = '<header><h2 id="cookieTitle">Preferencias de cookies</h2><button type="button" data-close aria-label="Cerrar preferencias">×</button></header><div class="cookie-dialog-body"><p>El almacenamiento necesario está siempre activo. Las categorías opcionales solo se cargan con tu permiso.</p><section class="cookie-option"><h3>Sesión y elección de privacidad <small>Siempre activas</small></h3><p>Mantienen el acceso a tu cuenta y recuerdan esta elección.</p></section><label class="cookie-option"><span><strong>Fuentes externas</strong><span>Permite cargar tipografías de Google Fonts. Si lo desactivas, se usan fuentes del dispositivo.</span></span><input type="checkbox" id="cookieFonts"></label><label class="cookie-option"><span><strong>Contenido incrustado</strong><span>Permite cargar paneles de terceros, como Power BI, que pueden utilizar sus propias cookies.</span></span><input type="checkbox" id="cookieExternal"></label><p class="cookie-storage-status" role="status"></p></div><footer><button type="button" data-save>Guardar preferencias</button><button type="button" data-all>Aceptar todo</button></footer>';
    document.body.append(banner, dialog);
    if (document.body.dataset.page === 'privacidad') document.querySelector('.legal-paper')?.append(reopen);
    const fonts = dialog.querySelector('#cookieFonts'), external = dialog.querySelector('#cookieExternal');
    function open() { fonts.checked = choice?.fonts === true; external.checked = choice?.external === true; dialog.showModal(); }
    function apply() {
        document.querySelectorAll('link[data-consent-font]').forEach(link => {
            if (choice?.fonts) link.href = link.dataset.consentFont;
            else link.removeAttribute('href');
        });
        document.querySelectorAll('iframe[data-consent-embed]').forEach(frame => {
            let placeholder = frame.previousElementSibling;
            if (!placeholder?.classList.contains('cookie-embed-placeholder')) {
                placeholder = document.createElement('div'); placeholder.className = 'cookie-embed-placeholder';
                placeholder.innerHTML = '<p>Este panel utiliza contenido externo. Puedes habilitarlo en tus preferencias.</p><button type="button">Configurar contenido externo</button>';
                placeholder.querySelector('button').addEventListener('click', open);
                frame.before(placeholder);
            }
            if (choice?.external) { if (frame.getAttribute('src') !== frame.dataset.consentEmbed) frame.src = frame.dataset.consentEmbed; }
            else frame.removeAttribute('src');
            frame.hidden = !choice?.external; placeholder.hidden = !!choice?.external;
        });
        banner.hidden = choice !== null;
        reopen.hidden = false;
    }
    function save(allowFonts, allowExternal) {
        choice = { version: 1, fonts: allowFonts, external: allowExternal };
        try { localStorage.setItem(key, JSON.stringify(choice)); } catch (_) {
            dialog.querySelector('.cookie-storage-status').textContent = 'Tu navegador no permite recordar esta elección entre páginas.';
            reopen.textContent = 'Preferencias de privacidad · elección temporal';
        }
        apply();
        if (dialog.open) dialog.close();
        if (reopen.isConnected) reopen.focus({ preventScroll: true });
    }
    banner.querySelector('[data-all]').addEventListener('click', () => save(true, true));
    banner.querySelector('[data-reject]').addEventListener('click', () => save(false, false));
    banner.querySelector('[data-manage]').addEventListener('click', open);
    reopen.addEventListener('click', open);
    dialog.querySelector('[data-close]').addEventListener('click', () => dialog.close());
    dialog.querySelector('[data-save]').addEventListener('click', () => save(fonts.checked, external.checked));
    dialog.querySelector('[data-all]').addEventListener('click', () => save(true, true));
    dialog.addEventListener('close', () => {
        const target = choice ? (reopen.isConnected ? reopen : null) : banner.querySelector('[data-manage]');
        target?.focus({ preventScroll: true });
    });
    // Avoid interrupting the short introduction; ask on the next page instead.
    apply();
    if (document.body.dataset.page === 'intro') { banner.hidden = true; reopen.hidden = true; }
})();
