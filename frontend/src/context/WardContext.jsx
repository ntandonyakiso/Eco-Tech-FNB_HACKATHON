import { createContext, useContext, useState, useEffect } from 'react';
import API from '../services/api';

const WardContext = createContext(null);

export function WardProvider({ children }) {
  const [wards, setWards] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    API.listWards()
      .then(setWards)
      .catch(() => setWards([]))
      .finally(() => setLoading(false));
  }, []);

  return (
    <WardContext.Provider value={{ wards, loading }}>
      {children}
    </WardContext.Provider>
  );
}

export function useWards() {
  const ctx = useContext(WardContext);
  if (!ctx) throw new Error('useWards must be used within WardProvider');
  return ctx;
}
