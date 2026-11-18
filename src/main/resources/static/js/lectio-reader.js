(() => {
    const chapters = [...document.querySelectorAll('.reader-chapter')];
    if (!chapters.length) return;
    const links = [...document.querySelectorAll('[data-chapter]')];
    const previous = document.getElementById('readerPrevious');
    const next = document.getElementById('readerNext');
    const smaller = document.getElementById('readerSmaller');
    const larger = document.getElementById('readerLarger');
    const theme = document.getElementById('readerTheme');
    const status = document.getElementById('readerSaveStatus');
    const reduce = matchMedia('(prefers-reduced-motion: reduce)');
    const key = document.body.dataset.readingId ? 'lectio-reading-' + document.body.dataset.readingId + '-v1' : 'lectio-demo-atlas-v1';
    let current = 0, fontSize = 19, night = false, animation;
    try {
        const saved = JSON.parse(localStorage.getItem(key) || 'null');
        if (saved) {
            if (Number.isInteger(saved.chapter)) current = Math.max(0, Math.min(chapters.length - 1, saved.chapter));
            if (Number.isFinite(saved.fontSize)) fontSize = Math.max(17, Math.min(25, saved.fontSize));
            night = saved.night === true;
        }
    } catch (_) { /* Reading works even when browser storage is unavailable. */ }
    function persist() {
        try {
            localStorage.setItem(key, JSON.stringify({ chapter: current, fontSize, night }));
            status.textContent = 'Tu punto de lectura se guarda en este navegador. No suma puntos a los desafíos.';
        } catch (_) {
            status.textContent = 'Puedes leer con normalidad; este navegador no permite guardar el punto de lectura.';
        }
    }
    function preferences() {
        document.body.style.setProperty('--reading-size', fontSize + 'px');
        document.body.classList.toggle('reader-night', night);
        theme.setAttribute('aria-pressed', String(night));
        theme.textContent = night ? 'Modo día' : 'Modo noche';
        smaller.disabled = fontSize <= 17;
        larger.disabled = fontSize >= 25;
    }
    function show(index, navigate = false) {
        current = Math.max(0, Math.min(chapters.length - 1, index));
        animation?.cancel();
        chapters.forEach((chapter, i) => { chapter.hidden = i !== current; });
        links.forEach((link, i) => {
            if (i === current) link.setAttribute('aria-current', 'step');
            else link.removeAttribute('aria-current');
        });
        previous.disabled = current === 0;
        next.disabled = current === chapters.length - 1;
        document.getElementById('readerPosition').textContent = `Capítulo ${current + 1} de ${chapters.length}`;
        document.getElementById('readerProgress').value = current + 1;
        persist();
        if (navigate) {
            const title = chapters[current].querySelector('h2');
            title.focus({ preventScroll: true });
            chapters[current].scrollIntoView({ behavior: reduce.matches ? 'instant' : 'smooth', block: 'start' });
            if (!reduce.matches && chapters[current].animate) {
                animation = chapters[current].animate([{ opacity: 0, transform: 'translateY(12px)' }, { opacity: 1, transform: 'none' }], { duration: 420, easing: 'cubic-bezier(.16,1,.3,1)' });
            }
        }
    }
    links.forEach((link, i) => link.addEventListener('click', event => { event.preventDefault(); show(i, true); }));
    previous.addEventListener('click', () => show(current - 1, true));
    next.addEventListener('click', () => show(current + 1, true));
    smaller.addEventListener('click', () => { fontSize = Math.max(17, fontSize - 2); preferences(); persist(); });
    larger.addEventListener('click', () => { fontSize = Math.min(25, fontSize + 2); preferences(); persist(); });
    theme.addEventListener('click', () => { night = !night; preferences(); persist(); });
    const hashIndex = chapters.findIndex(chapter => '#' + chapter.id === location.hash);
    if (hashIndex >= 0) current = hashIndex;
    document.querySelectorAll('.reader-tools, .reader-controls, .reader-progress').forEach(element => { element.hidden = false; });
    preferences();
    show(current);
    reduce.addEventListener('change', () => { if (reduce.matches) animation?.cancel(); });
})();
