import axios, { AxiosRequestConfig } from "axios";

const API_BASE_URL = "http://localhost:8080";

export const apiAxios = async <T = any,>(
  path: string,
  options?: AxiosRequestConfig
): Promise<T> => {
  const res = await axios(`${API_BASE_URL}${path}`, {
    headers: { "Content-Type": "application/json" },
    ...options,
  });

  if (res.data && typeof res.data === "object" && "data" in res.data) {
    return (res.data as { data: T }).data;
  }

  return res.data as T;
};
