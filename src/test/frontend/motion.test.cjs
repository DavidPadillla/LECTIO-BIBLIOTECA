const { test } = require('node:test');
const assert = require('node:assert/strict');
const fs = require('node:fs');
const vm = require('node:vm');
const script = fs.readFileSync('src/main/resources/static/js/lectio-ui.js', 'utf8');

function run({ reduced = false, observerAvailable = true, observerFails = false } = {}) {
    const nodes = [50, 1500, 4000].map(top => {
        const classes = new Set();
        return {
            classes,
            classList: { add: name => classes.add(name), remove: name => classes.delete(name) },
            getBoundingClientRect: () => ({ top })
        };
    });
    const mediaListeners = {};
    const windowListeners = {};
    let intersection;
    const observed = [];
    const window = {
        innerHeight: 800,
        matchMedia: () => ({ matches: reduced, addEventListener: (event, fn) => mediaListeners[event] = fn }),
        addEventListener: (event, fn) => windowListeners[event] = fn
    };
    class Observer {
        constructor(callback) {
            if (observerFails) throw new Error('Observer unavailable');
            intersection = callback;
        }
        observe(node) { observed.push(node); }
        unobserve() {}
        disconnect() {}
    }
    if (observerAvailable) window.IntersectionObserver = Observer;
    const document = {
        querySelectorAll: selector => selector === '.reveal, .stagger > *' ? nodes : [],
        querySelector: () => null,
        getElementById: () => null
    };
    vm.runInNewContext(script, { window, document, IntersectionObserver: Observer });
    return { nodes, observed, mediaListeners, windowListeners, intersect: () => intersection([{ target: nodes[2], isIntersecting: true }]) };
}

test('initial viewport stays visible and a deep item reveals independently of its grid', () => {
    const state = run();
    assert.equal(state.nodes[0].classes.size, 0);
    assert.equal(state.observed.length, 2);
    state.intersect();
    assert.equal(state.nodes[2].classes.size, 0);
});

test('content remains visible without an observer or when its creation fails', () => {
    for (const options of [{ observerAvailable: false }, { observerFails: true }]) {
        assert.ok(run(options).nodes.every(node => node.classes.size === 0));
    }
});

test('reduced motion never hides content and preference changes reveal pending items', () => {
    assert.ok(run({ reduced: true }).nodes.every(node => node.classes.size === 0));
    const state = run();
    state.mediaListeners.change();
    assert.ok(state.nodes.every(node => node.classes.size === 0));
});

test('restoring a page from browser history clears pending animations', () => {
    const state = run();
    state.windowListeners.pageshow({ persisted: true });
    assert.ok(state.nodes.every(node => node.classes.size === 0));
});
