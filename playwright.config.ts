import { defineConfig } from "@playwright/test"
export default defineConfig({
  testDir: "./tests/browser",
  workers: 1,
  fullyParallel: false,
  timeout: 30000,
  use: {
    baseURL: "http://127.0.0.1:4178/",
    browserName: "chromium",
    headless: true,
    trace: "retain-on-failure",
  },
  webServer: {
    command: "pnpm preview",
    port: 4178,
    reuseExistingServer: !process.env.CI,
  },
})
