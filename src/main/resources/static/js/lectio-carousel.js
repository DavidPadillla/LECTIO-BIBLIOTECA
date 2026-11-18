(() => {
    const reduce = matchMedia('(prefers-reduced-motion: reduce)');
    document.querySelectorAll('.journey-worlds, .libros-grid').forEach(track => {
        const cards = [...track.children];
        if (cards.length < 2) return;
        track.classList.add('journey-carousel');
        track.setAttribute('aria-label', 'Desafíos de lectura');
        track.setAttribute('aria-roledescription', 'carrusel');
        const controls = document.createElement('div');
        controls.className = 'journey-carousel-controls';
        controls.innerHTML = '<button type="button" data-prev aria-label="Ver libros anteriores">←</button><span class="carousel-position"></span><button type="button" data-next aria-label="Ver más libros">→</button>';
        track.after(controls);
        let timer;
        let animation;
        const stopMotion = () => {
            cancelAnimationFrame(animation);
            track.classList.remove('is-gliding');
        };
        const glideTo = target => {
            stopMotion();
            const start = track.scrollLeft;
            const destination = Math.max(0, Math.min(target, track.scrollWidth - track.clientWidth));
            if (reduce.matches) {
                track.scrollTo({ left: destination, behavior: 'instant' });
                return;
            }
            const distance = destination - start;
            const duration = Math.abs(distance) > track.clientWidth ? 1200 : 850;
            const started = performance.now();
            track.classList.add('is-gliding');
            const frame = now => {
                const progress = Math.min(1, (now - started) / duration);
                // Gentle acceleration and deceleration, without a sudden final snap.
                const eased = progress * progress * progress * (progress * (progress * 6 - 15) + 10);
                track.scrollTo({ left: start + distance * eased, behavior: 'instant' });
                if (progress < 1) animation = requestAnimationFrame(frame);
                else stopMotion();
            };
            animation = requestAnimationFrame(frame);
        };
        const index = () => cards.reduce((best, card, i) => Math.abs(card.getBoundingClientRect().left - track.getBoundingClientRect().left) < Math.abs(cards[best].getBoundingClientRect().left - track.getBoundingClientRect().left) ? i : best, 0);
        const update = () => {
            controls.querySelector('.carousel-position').textContent = `${index() + 1} / ${cards.length}`;
            controls.querySelector('[data-prev]').disabled = track.scrollLeft < 4;
            controls.querySelector('[data-next]').disabled = track.scrollLeft >= track.scrollWidth - track.clientWidth - 4;
        };
        const move = (direction, auto = false) => {
            const end = track.scrollLeft >= track.scrollWidth - track.clientWidth - 4;
            const i = auto && end ? 0 : Math.max(0, Math.min(cards.length - 1, index() + direction));
            const target = cards[i].getBoundingClientRect().left - track.getBoundingClientRect().left + track.scrollLeft;
            glideTo(target);
        };
        const schedule = () => {
            clearInterval(timer);
            if (!reduce.matches && !document.hidden) timer = setInterval(() => {
                const rect = track.getBoundingClientRect();
                if (rect.bottom > 0 && rect.top < innerHeight) move(1, true);
            }, 3500);
            update();
        };
        controls.querySelector('[data-prev]').addEventListener('click', () => { move(-1); schedule(); });
        controls.querySelector('[data-next]').addEventListener('click', () => { move(1); schedule(); });
        track.addEventListener('pointerdown', () => { stopMotion(); schedule(); }, { passive: true });
        track.addEventListener('wheel', () => { stopMotion(); schedule(); }, { passive: true });
        track.addEventListener('keydown', stopMotion);
        track.addEventListener('scroll', update, { passive: true });
        window.addEventListener('resize', () => { stopMotion(); update(); });
        document.addEventListener('visibilitychange', () => { stopMotion(); schedule(); });
        reduce.addEventListener('change', () => { stopMotion(); schedule(); });
        schedule();
    });
})();
