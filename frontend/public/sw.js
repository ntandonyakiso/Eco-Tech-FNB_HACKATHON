const CACHE_NAME = 'ecotech-v1';
const ASSETS = [
  '/',
  '/index.html',
  '/assets/index-C8guRl9s.js',
  '/assets/index-BAXfRjAl.css',
];

self.addEventListener('install', (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => cache.addAll(ASSETS))
  );
});

self.addEventListener('fetch', (event) => {
  event.respondWith(
    caches.match(event.request).then((response) => response || fetch(event.request))
  );
});

self.addEventListener('push', (event) => {
  if (!event.data) return;
  const data = event.data.text();
  event.waitUntil(
    self.registration.showNotification('Eco-Tech', {
      body: data,
      icon: '/icon-192.png',
    })
  );
});
