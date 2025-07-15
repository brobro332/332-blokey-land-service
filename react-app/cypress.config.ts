import { defineConfig } from "cypress";
import * as dotenv from "dotenv";

dotenv.config({ path: ".env.development" });

export default defineConfig({
  e2e: {
    supportFile: false,
    baseUrl: process.env.CYPRESS_BASE_URL,
  },
});
