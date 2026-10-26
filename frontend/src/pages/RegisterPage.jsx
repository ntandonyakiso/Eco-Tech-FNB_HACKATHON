import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import API from '../services/api';
import { useAuth } from '../context/AuthContext';
import { useWards } from '../context/WardContext';

export default function RegisterPage() {
  const [identifier, setIdentifier] = useState('');
  const [password, setPassword] = useState('');
  const [type, setType] = useState('PHONE');
  const [language, setLanguage] = useState('en');
  const [wardName, setWardName] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const { wards } = useWards();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      const res = await API.register({
        identifier,
        password,
        type,
        language,
        wardName,
      });
      login(res.token, {
        userId: res.userId,
        role: res.role,
        wardId: res.wardId,
        language: res.language,
      });
      navigate('/');
    } catch (err) {
      setError(err.message || 'Registration failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center p-4">
      <div className="w-full max-w-md space-y-6">
        <h1 className="text-2xl font-bold text-center">Create account</h1>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium mb-1">Phone or email</label>
            <input
              type="text"
              className="w-full border rounded px-3 py-2"
              placeholder={type === 'PHONE' ? '+27 72 123 4567' : 'you@example.com'}
              value={identifier}
              onChange={(e) => setIdentifier(e.target.value)}
              required
            />
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">Password</label>
            <input
              type="password"
              className="w-full border rounded px-3 py-2"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          <div className="flex gap-4">
            <label className="flex items-center gap-2 cursor-pointer">
              <input type="radio" name="authType" value="PHONE" checked={type === 'PHONE'} onChange={(e) => setType(e.target.value)} /> Phone
            </label>
            <label className="flex items-center gap-2 cursor-pointer">
              <input type="radio" name="authType" value="EMAIL" checked={type === 'EMAIL'} onChange={(e) => setType(e.target.value)} /> Email
            </label>
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">Language</label>
            <select className="w-full border rounded px-3 py-2" value={language} onChange={(e) => setLanguage(e.target.value)}>
              <option value="en">English</option>
              <option value="zu">isiZulu</option>
              <option value="xh">isiXhosa</option>
              <option value="af">Afrikaans</option>
              <option value="st">Sesotho</option>
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">Ward</label>
            <select className="w-full border rounded px-3 py-2" value={wardName} onChange={(e) => setWardName(e.target.value)} required>
              <option value="">Choose a ward</option>
              {wards.map((w) => (
                <option key={w.id} value={w.name}>
                  {w.wardNumber ? 'Ward ' + w.wardNumber : ''} {w.name} ({w.province})
                </option>
              ))}
            </select>
          </div>
          {error && <p className="text-sm text-red-600 text-center">{error}</p>}
          <button
            type="submit"
            disabled={loading}
            className="w-full bg-green-600 text-white py-2 rounded disabled:opacity-50"
          >
            {loading ? 'Creating...' : 'Create account'}
          </button>
        </form>
        <p className="text-center text-sm text-gray-600">
          Already have an account? <a href="/login" className="text-green-600">Log in</a>
        </p>
      </div>
    </div>
  );
}
