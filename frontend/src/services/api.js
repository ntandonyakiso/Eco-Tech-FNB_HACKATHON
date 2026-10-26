const API_BASE = import.meta.env.VITE_API_BASE || '/api';
const API_ENV = import.meta.env.VITE_API_ENV || 'web'; // web | emulator | device | production

function resolveBaseUrl() {
  if (API_BASE !== '/api') return API_BASE; // explicit override
  switch (API_ENV) {
    case 'emulator': return 'http://10.0.2.2:8080/api';
    case 'device':   return 'http://' + (import.meta.env.VITE_DEVICE_IP || '192.168.1.100') + ':8080/api';
    case 'production': return 'https://eco-tech-backend.onrender.com/api';
    default: return '/api';
  }
}

const BASE_URL = resolveBaseUrl();

const API = {
  async request(path, options = {}) {
    const token = localStorage.getItem('token');
    const headers = {
      'Content-Type': 'application/json',
      ...options.headers,
    };
    if (token) headers['Authorization'] = 'Bearer ' + token;
    const res = await fetch(BASE_URL + path, { ...options, headers });
    if (!res.ok) {
      const body = await res.json().catch(() => ({}));
      throw new Error(body.message || res.statusText);
    }
    if (res.status === 204) return null;
    return res.json();
  },

  register(data) { return this.request('/v1/auth/register', { method: 'POST', body: JSON.stringify(data) }); },
  login(data) { return this.request('/v1/auth/login', { method: 'POST', body: JSON.stringify(data) }); },
  listWards() { return this.request('/v1/wards'); },

  // Reports
  createReport(formData) {
    const token = localStorage.getItem('token');
    return fetch(API_BASE + '/v1/reports', {
      method: 'POST',
      headers: token ? { Authorization: 'Bearer ' + token } : {},
      body: formData,
    }).then(async (res) => {
      if (!res.ok) {
        const body = await res.json().catch(() => ({}));
        throw new Error(body.message || res.statusText);
      }
      return res.json();
    });
  },
  listReports(params = {}) {
    const qs = new URLSearchParams();
    if (params.status) qs.set('status', params.status);
    if (params.category) qs.set('category', params.category);
    if (params.wardId) qs.set('wardId', params.wardId);
    qs.set('page', params.page ?? 0);
    qs.set('size', params.size ?? 20);
    return this.request('/v1/reports?' + qs.toString());
  },
  getReport(id) { return this.request('/v1/reports/' + id); },
  upvoteReport(id) {
    return this.request('/v1/reports/' + id + '/upvote', { method: 'POST' });
  },
  changeStatus(id, body) {
    return this.request('/v1/reports/' + id + '/status', {
      method: 'PATCH',
      body: JSON.stringify(body),
    });
  },

  // Alerts
  listAlerts() { return this.request('/v1/alerts'); },
  ingestAlerts() { return this.request('/v1/alerts/ingest', { method: 'POST' }); },

  // Subscriptions
  listSubscriptions() { return this.request('/v1/subscriptions'); },
  setSubscription(channel, alertType, enabled) {
    return this.request('/v1/subscriptions', {
      method: 'POST',
      body: JSON.stringify({ channel, alertType, enabled }),
    });
  },

  // Push
  vapidPublicKey() { return this.request('/v1/push-subscribe/vapid-public-key'); },
  savePushSubscription(endpoint, p256dhKey, authKey) {
    return this.request('/v1/push-subscribe', {
      method: 'POST',
      body: JSON.stringify({ endpoint, p256dhKey: p256dhKey, authKey }),
    });
  },
  removePushSubscription(endpoint) {
    return this.request('/v1/push-subscribe?endpoint=' + encodeURIComponent(endpoint), { method: 'DELETE' });
  },

  // Telegram
  linkTelegram(chatId) {
    return this.request('/v1/telegram/link', {
      method: 'POST',
      body: JSON.stringify({ chatId }),
    });
  },

  // Resource share
  listResources(params = {}) {
    const qs = new URLSearchParams();
    if (params.category) qs.set('category', params.category);
    if (params.lat != null) qs.set('lat', params.lat);
    if (params.lon != null) qs.set('lon', params.lon);
    if (params.radiusKm != null) qs.set('radiusKm', params.radiusKm);
    qs.set('page', params.page ?? 0);
    qs.set('size', params.size ?? 20);
    return this.request('/v1/resources?' + qs.toString());
  },
  createResource(data) {
    return this.request('/v1/resources', { method: 'POST', body: JSON.stringify(data) });
  },
  getResource(id) { return this.request('/v1/resources/' + id); },
  updateResource(id, data) {
    return this.request('/v1/resources/' + id, { method: 'PUT', body: JSON.stringify(data) });
  },
  deleteResource(id) {
    return this.request('/v1/resources/' + id, { method: 'DELETE' });
  },
  myListings() { return this.request('/v1/resources/my-listings'); },
  requestResource(listingId, message) {
    return this.request('/v1/resources/' + listingId + '/requests', {
      method: 'POST',
      body: JSON.stringify({ listingId, message }),
    });
  },
  respondToRequest(requestId, status) {
    return this.request('/v1/resources/requests/' + requestId + '?status=' + status, { method: 'PATCH' });
  },
  myRequests() { return this.request('/v1/resources/requests/my-requests'); },
  requestsForListing(listingId) {
    return this.request('/v1/resources/' + listingId + '/requests');
  },

  // Vulnerable contacts
  createVulnerableContact(data) {
    return this.request('/v1/vulnerable-contacts', { method: 'POST', body: JSON.stringify(data) });
  },
  listVulnerableContacts() { return this.request('/v1/vulnerable-contacts'); },
  deleteVulnerableContact(id) {
    return this.request('/v1/vulnerable-contacts/' + id, { method: 'DELETE' });
  },

  // Usage logs
  logUsage(data) {
    return this.request('/v1/usage', { method: 'POST', body: JSON.stringify(data) });
  },
  listUsage(params = {}) {
    const qs = new URLSearchParams();
    qs.set('page', params.page ?? 0);
    qs.set('size', params.size ?? 20);
    return this.request('/v1/usage?' + qs.toString());
  },
  usageSummary() { return this.request('/v1/usage/summary'); },

  // Business profile
  upsertBusinessProfile(data) {
    return this.request('/v1/business/profile', { method: 'POST', body: JSON.stringify(data) });
  },
  getBusinessProfile() { return this.request('/v1/business/profile'); },
  outageCost(data) {
    return this.request('/v1/business/outage-cost', { method: 'POST', body: JSON.stringify(data) });
  },
  backupComparison(data) {
    return this.request('/v1/business/backup-comparison', { method: 'POST', body: JSON.stringify(data) });
  },

  // Stock loss
  logStockLoss(data) {
    return this.request('/v1/business/stock-loss', { method: 'POST', body: JSON.stringify(data) });
  },
  listStockLosses(params = {}) {
    const qs = new URLSearchParams();
    qs.set('page', params.page ?? 0);
    qs.set('size', params.size ?? 20);
    return this.request('/v1/business/stock-loss?' + qs.toString());
  },

  // Food points
  listFoodPoints() { return this.request('/v1/food-points'); },
  addFoodPoint(data) {
    return this.request('/v1/food-points', { method: 'POST', body: JSON.stringify(data) });
  },
  myFoodPoints() { return this.request('/v1/food-points/mine'); },

  // Surplus posts
  listSurplusPosts() { return this.request('/v1/surplus-posts'); },
  createSurplusPost(data) {
    return this.request('/v1/surplus-posts', { method: 'POST', body: JSON.stringify(data) });
  },
  mySurplusPosts() { return this.request('/v1/surplus-posts/mine'); },
  claimSurplusPost(id) {
    return this.request('/v1/surplus-posts/' + id + '/claim', { method: 'POST' });
  },
  deleteSurplusPost(id) {
    return this.request('/v1/surplus-posts/' + id, { method: 'DELETE' });
  },

  // Preparedness plan
  getPreparednessPlan() { return this.request('/v1/preparedness'); },
  savePreparednessPlan(data) {
    return this.request('/v1/preparedness', { method: 'POST', body: JSON.stringify(data) });
  },
};

export default API;
