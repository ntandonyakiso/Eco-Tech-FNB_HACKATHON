import { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import API from '../services/api';

const ALERT_TYPES = ['WEATHER', 'LOAD_SHEDDING', 'WATER', 'FLOOD', 'HEAT'];
const CHANNELS = ['TELEGRAM', 'WEB_PUSH', 'SMS'];

export default function AlertsPage() {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [alerts, setAlerts] = useState([]);
  const [subs, setSubs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [vapidKey, setVapidKey] = useState(null);
  const [pushSubscribed, setPushSubscribed] = useState(false);
  const [telegramChatId, setTelegramChatId] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const [a, s, v] = await Promise.all([API.listAlerts(), API.listSubscriptions(), API.vapidPublicKey()]);
      setAlerts(a);
      setSubs(s);
      setVapidKey(v.publicKey);
    } catch (err) {
      setError(err.message || 'Failed to load');
    } finally {
      setLoading(false);
    }
  };

  const isSubscribed = (channel, type) => subs.includes(channel + ':' + type);

  const toggleSub = async (channel, type) => {
    const currently = isSubscribed(channel, type);
    try {
      await API.setSubscription(channel, type, !currently);
      setSubs(subs.filter(x => x !== channel + ':' + type));
      if (!currently) setSubs([...subs, channel + ':' + type]);
    } catch (err) {
      setError(err.message || 'Failed to update subscription');
    }
  };

  const registerPush = async () => {
    if (!('serviceWorker' in navigator) || !('PushManager' in window)) {
      setError('Push not supported in this browser');
      return;
    }
    try {
      const registration = await navigator.serviceWorker.ready;
      const subscription = await registration.pushManager.subscribe({
        userVisibleOnly: true,
        applicationServerKey: vapidKey ? urlBase64ToUint8Array(vapidKey) : undefined,
      });
      const raw = subscription.toJSON();
      await API.savePushSubscription(raw.endpoint, raw.keys.p256dh, raw.keys.auth);
      setPushSubscribed(true);
    } catch (err) {
      setError('Push subscription failed: ' + err.message);
    }
  };

  const linkTelegram = async () => {
    try {
      await API.linkTelegram(telegramChatId);
      setError('Telegram chat linked (check your bot for confirmation)');
    } catch (err) {
      setError(err.message || 'Failed to link Telegram');
    }
  };

  if (loading) return <div className="p-4">Loading alerts...</div>;

  return (
    <div className="min-h-screen p-2 md:p-4">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-xl font-bold">Local alerts</h1>
        <button
          onClick={() => API.ingestAlerts().then(() => loadData())}
          className="px-3 py-1 bg-green-600 text-white rounded text-sm"
        >
          Refresh alerts
        </button>
      </div>

      <div className="grid md:grid-cols-2 gap-6">
        <div className="bg-white p-4 rounded border shadow-sm">
          <h2 className="font-bold mb-3">Active alerts for your ward</h2>
          {alerts.length === 0 ? (
            <p className="text-sm text-gray-600">No active alerts.</p>
          ) : (
            <div className="space-y-2">
              {alerts.map((a) => (
                <div key={a.id} className="p-3 border rounded bg-orange-50">
                  <div className="flex justify-between mb-1">
                    <span className="font-semibold text-sm">{a.title}</span>
                    <span className="text-xs px-1.5 py-0.5 rounded bg-gray-200">{a.severity}</span>
                  </div>
                  <p className="text-sm text-gray-700">{a.body}</p>
                  <p className="text-xs text-gray-500">{a.source} • {new Date(a.startsAt).toLocaleString()}</p>
                </div>
              ))}
            </div>
          )}
        </div>

        <div className="bg-white p-4 rounded border shadow-sm">
          <h2 className="font-bold mb-3">Notification channels</h2>
          <p className="text-sm text-gray-600 mb-4">Choose how you receive alerts.</p>
          <div className="space-y-3">
            {CHANNELS.map((channel) => (
              <div key={channel}>
                <div className="font-medium text-sm mb-2">{channel}</div>
                <div className="flex gap-2">
                  {ALERT_TYPES.map((type) => (
                    <label key={type} className="flex items-center gap-1 cursor-pointer text-sm">
                      <input
                        type="checkbox"
                        checked={isSubscribed(channel, type)}
                        onChange={() => toggleSub(channel, type)}
                      />
                      {type}
                    </label>
                  ))}
                </div>
              </div>
            ))}
          </div>

          <div className="mt-6 pt-4 border-t">
            <h3 className="font-medium text-sm mb-2">Web Push</h3>
            <p className="text-xs text-gray-600 mb-2">{vapidKey ? 'VAPID key configured' : 'VAPID not configured — push disabled'}</p>
            <button
              onClick={registerPush}
              disabled={pushSubscribed || !vapidKey}
              className={`px-3 py-1 rounded text-sm ${pushSubscribed || !vapidKey ? 'bg-gray-300' : 'bg-green-600 text-white'}`}
            >
              {pushSubscribed ? 'Subscribed' : 'Subscribe to push'}
            </button>
          </div>

          <div className="mt-4 pt-4 border-t">
            <h3 className="font-medium text-sm mb-2">Telegram</h3>
            <p className="text-xs text-gray-600 mb-2">Send your chat ID from the /start command in the Eco-Tech Telegram bot.</p>
            <div className="flex gap-2">
              <input
                type="text"
                className="flex-1 border rounded px-2 py-1 text-sm"
                placeholder="chat_id"
                value={telegramChatId}
                onChange={(e) => setTelegramChatId(e.target.value)}
              />
              <button onClick={linkTelegram} className="px-3 py-1 bg-green-600 text-white rounded text-sm">
                Link
              </button>
            </div>
          </div>
        </div>
      </div>

      {error && <p className="mt-4 text-sm text-red-600">{error}</p>}
      <p className="mt-6 text-sm text-gray-600">
        <Link to="/" className="text-green-600">← Home</Link>
      </p>
    </div>
  );
}

function urlBase64ToUint8Array(base64String) {
  const padding = '='.repeat((4 - (base64String.length % 4)) % 4);
  const base64 = (base64String + padding).replace(/-/g, '+').replace(/_/g, '/');
  const raw = atob(base64);
  return Uint8Array.from([...raw].map((c) => c.charCodeAt(0)));
}
