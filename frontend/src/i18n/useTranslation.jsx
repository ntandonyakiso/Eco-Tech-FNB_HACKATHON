import { useState, useCallback } from 'react';

const translations = {
  en: require('./locales/en.json'),
  zu: require('./locales/zu.json'),
  xh: require('./locales/xh.json'),
  af: require('./locales/af.json'),
  st: require('./locales/st.json'),
};

const LANGUAGE_NAMES = {
  en: 'English',
  zu: 'isiZulu',
  xh: 'isiXhosa',
  af: 'Afrikaans',
  st: 'Sesotho',
};

export function useTranslation() {
  const [lang, setLang] = useState(() => {
    if (typeof window !== 'undefined') {
      const saved = localStorage.getItem('ecotech-lang');
      return saved && translations[saved] ? saved : 'en';
    }
    return 'en';
  });

  const t = useCallback((key) => {
    const dict = translations[lang] || translations.en;
    const keys = key.split('.');
    let val = dict;
    for (const k of keys) {
      if (val == null || typeof val !== 'object') return key;
      val = val[k];
    }
    return typeof val === 'string' ? val : key;
  }, [lang]);

  const changeLanguage = (newLang) => {
    if (translations[newLang]) {
      localStorage.setItem('ecotech-lang', newLang);
      setLang(newLang);
    }
  };

  return { t, lang, changeLanguage, languages: LANGUAGE_NAMES };
}

export default useTranslation;
