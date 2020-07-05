const PROXY_CONFIG = [
  {
    context: [
      "/api",
      "/many",
      "/endpoints",
      "/i",
      "/need",
      "/to",
      "/proxy"
    ],
    target: "http://localhost:8080",
    secure: false
  }
]

module.exports = PROXY_CONFIG;
