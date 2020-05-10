'use strict';
const CACHE_NAME = 'flutter-app-cache';
const RESOURCES = {
  "icons/Icon-192.png": "ac9a721a12bbc803b44f645561ecb1e1",
"icons/Icon-512.png": "96e752610906ba2a93c65f8abe1645f1",
"manifest.json": "8c76aa4df182e5cdae50eb4d9fb9fcba",
"main.dart.js": "ca41006675d32b99821ced8066f960e8",
"index.html": "d13c5250e5018aa52d6d90698cce858a",
"/": "d13c5250e5018aa52d6d90698cce858a",
"assets/FontManifest.json": "580ff1a5d08679ded8fcf5c6848cece7",
"assets/AssetManifest.json": "ea5bca934c81853ddfb4581ac5c43c10",
"assets/LICENSE": "c98da95c70612cf417cbeb9431716ae5",
"assets/fonts/MaterialIcons-Regular.ttf": "56d3ffdef7a25659eab6a68a3fbfaf16",
"assets/assets/logo.png": "4efc8914d5b94891ed9a8d6f1ea87848",
"assets/assets/maps/f4.png": "5d79ff1d60d7101898fffb1ff572ed6e",
"assets/assets/maps/m2.png": "65938c8140a18926dcbed633219dd4f6",
"assets/assets/maps/f3.png": "45a8432d2ad6d801e9d25bf3211c9b23",
"assets/assets/maps/m1.png": "178fd33554f0d61408b44efe0448502c",
"assets/assets/maps/f1.png": "70cb5ff2a10f7215fba0223cb08ad964",
"assets/assets/maps/mG.png": "89cf5f83c4db42866bbbdcc4d164b18c",
"assets/assets/maps/mL1.png": "0eeba2524ce3155e6a1d657c742ea838",
"assets/assets/maps/f2.png": "b5407c0036c9ad436f954acd18d344fb",
"assets/assets/maps/mL2.png": "8f97f35c0b04f4f91fb4efa837e79463",
"assets/assets/maps/m3.png": "904ebb987a0e51593a00062b65a0915c",
"assets/assets/maps/f5.png": "0b63c2902c930ce6b9f3c400cec04ffd",
"favicon.png": "4efc8914d5b94891ed9a8d6f1ea87848"
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
