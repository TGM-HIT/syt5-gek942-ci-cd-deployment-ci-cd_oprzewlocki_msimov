import axios from 'axios';

export const http = axios.create({
  baseURL: import.meta.env.VITE_API_BASE,
  timeout: 15000
});

// optional: log errors centrally
http.interceptors.response.use(
  r => r,
  err => {
    console.error('[HTTP]', err?.response?.status, err?.message);
    throw err;
  }
);

/** composite key helper: turns {s_stamp, s_id} into "s_id,s_stamp" and URL-encodes */
export function encodeCompositeKey(parts: (string|number)[]) {
  return encodeURIComponent(parts.join(','));
}
export function decodeCompositeKey(encoded: string) {
  return decodeURIComponent(encoded).split(',');
}
