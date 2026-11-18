(() => {
    const mobile = matchMedia('(max-width: 768px)');
    const reduce = matchMedia('(prefers-reduced-motion: reduce)');
    const video = document.getElementById('intro-video');
    const loading = document.getElementById('loading');
    let timer;
    const enter = () => location.replace('/api/index');
    function start() {
        clearTimeout(timer);
        video.pause();
        video.removeAttribute('src');
        video.replaceChildren();
        video.load();
        document.body.classList.toggle('mobile-intro', mobile.matches);
        loading.hidden = mobile.matches;
        if (mobile.matches) {
            // Mobile never receives a video source or downloads the desktop file.
            if (!reduce.matches) timer = setTimeout(enter, 4000);
            return;
        }
        if (reduce.matches) { enter(); return; }
        video.src = '/img/NUEVO2.mp4';
        video.play().catch(enter);
        timer = setTimeout(enter, 30000);
    }
    video.addEventListener('loadeddata', () => { loading.hidden = true; });
    video.addEventListener('ended', () => { if (!mobile.matches) enter(); });
    video.addEventListener('error', () => { if (!mobile.matches) enter(); });
    document.addEventListener('keydown', event => { if (event.key === 'Escape') enter(); });
    mobile.addEventListener('change', start);
    reduce.addEventListener('change', start);
    start();
})();
