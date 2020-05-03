'use strict';
const CACHE_NAME = 'flutter-app-cache';
const RESOURCES = {
  "/icons/Icon-192.png": "ac9a721a12bbc803b44f645561ecb1e1",
"/icons/Icon-512.png": "96e752610906ba2a93c65f8abe1645f1",
"/manifest.json": "8c76aa4df182e5cdae50eb4d9fb9fcba",
"/main.dart.js": "b307193a21fc3a4a65fe1dff05d787cb",
"/index.html": "d13c5250e5018aa52d6d90698cce858a",
"/assets/FontManifest.json": "580ff1a5d08679ded8fcf5c6848cece7",
"/assets/AssetManifest.json": "9c799c883f040e52c4dd4f7f6cef925c",
"/assets/LICENSE": "08d5b7644bec8b5a5b1ca0f497d08442",
"/assets/fonts/MaterialIcons-Regular.ttf": "56d3ffdef7a25659eab6a68a3fbfaf16",
"/assets/assets/logo.png": "4efc8914d5b94891ed9a8d6f1ea87848",
"/assets/assets/floors/4.png": "27d106458122ea2ca6b83407e2e50071",
"/assets/assets/floors/5.png": "fe247a9e2b89e5f7a7b03a768c95e1c9",
"/assets/assets/floors/2.png": "8b3a3b0fda35d92150fae4579a47a5c4",
"/assets/assets/floors/3.png": "691f0b98837f5cae02513a63f08a654a",
"/assets/assets/floors/1.png": "c079b281ef519570ae60811b6b1fdebe",
"/favicon.png": "4efc8914d5b94891ed9a8d6f1ea87848"
};

self.addEventListener('activate', function (event) {
  event.waitUntil(
    caches.keys().then(function (cacheName) {
      return caches.delete(cacheName);
    }).then(function (_) {
      return caches.open(CACHE_NAME);
    }).then(function (cache) {
      return cache.addAll(Object.keys(RESOURCES));
    })
  );
});

self.addEventListener('fetch', function (event) {
  event.respondWith(
    caches.match(event.request)
      .then(function (response) {
        if (response) {
          return response;
        }
        return fetch(event.request);
      })
  );
});
