# 🥽 Rokid OpenClaw

**Rokid AI Glasses + OpenClaw — glasses-direct, voice-first wearable AI**

The Rokid AI Glasses connect **directly** to [OpenClaw Gateway](https://openclaw.ai) over WiFi — no phone needed. Talk to Claude, capture your world through the 12MP camera, and get AI responses on your monochrome green HUD.

---

## ✨ Planned Features

| Feature | Description |
|---------|-------------|
| 🎤 **Voice Input** | 4-mic array captures speech, transcribed and sent to OpenClaw |
| 📸 **Camera Capture** | 12MP camera frames sent to Claude's vision API |
| 🖥️ **HUD Display** | AI responses on 480×640 monochrome green micro-LED |
| 🔊 **TTS Responses** | Claude speaks back via built-in speakers |
| 🔗 **Session Management** | Persistent conversation sessions via OpenClaw Gateway |
| 📡 **Wake Word** | Always-on listening with configurable wake word |
| 🗺️ **Context Awareness** | Location, time, and sensor data piped into prompts |

---

## 🏗️ Architecture

The Rokid AI Glasses run Android internally and can run sideloaded Jetpack Compose APKs. They connect directly to OpenClaw Gateway over WiFi — no phone middleman required.

```
┌─────────────────────────────────────────────────────────────────┐
│                      Rokid AI Glasses                           │
│                                                                 │
│  🎤 4-mic array ──► Voice capture & transcription               │
│  📸 12MP camera ──► Frame capture for Claude vision             │
│  🖥️ 480×640 green micro-LED ◄── HUD chat display              │
│  🔊 Built-in speakers ◄── TTS playback                         │
│  📡 WiFi ──► WebSocket client                                   │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │  WiFi / WebSocket
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                     OpenClaw Gateway                            │
│  • Claude API (Anthropic)                                       │
│  • Tool execution (web search, memory, etc.)                    │
│  • Session persistence                                          │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                         Claude                                  │
│  • Vision (camera frames)                                       │
│  • Conversation                                                 │
│  • Tool use                                                     │
└─────────────────────────────────────────────────────────────────┘
```

**No phone bridge needed** — the glasses app handles everything:
- **`glasses-app/`** — Standalone Android app (Kotlin/Jetpack Compose) running on the glasses
- **`phone-app/`** — Reserved for future Rokid Max/AR line (requires phone bridge)
- **`shared/`** — Protocol definitions shared between components

---

## 🙏 Inspiration

This project is directly inspired by **[Clawsses](https://github.com/dweddepohl/clawsses)** by [@dweddepohl](https://github.com/dweddepohl) — a Claude-powered smart glasses app. Clawsses proved the concept beautifully; Rokid OpenClaw adapts it for the Rokid AI Glasses with a direct WiFi connection to OpenClaw's persistent session architecture.

Go give Clawsses a ⭐!

---

## 📋 Requirements

- [Android Studio](https://developer.android.com/studio) (latest stable)
- [OpenClaw Gateway](https://openclaw.ai) — running locally or remote
- Rokid AI Glasses (with WiFi connectivity)
- WiFi network accessible to both glasses and Gateway

---

## 📚 Resources

- **Rokid Developer Docs:** https://developer.rokid.com/
- **OpenClaw:** https://openclaw.ai
- **Clawsses (inspiration):** https://github.com/dweddepohl/clawsses
- **Anthropic Claude API:** https://docs.anthropic.com

---

## 🚀 Getting Started

> 🚧 This project is in early scaffolding phase. Check back soon!

1. Clone this repo
2. Open `glasses-app/` in Android Studio
3. Build and sideload the APK to your Rokid AI Glasses
4. Connect the glasses to your WiFi network
5. Configure your OpenClaw Gateway URL in the app settings
6. Say the wake word and start chatting!

---

## 📁 Project Structure

```
rokid-openclaw/
├── glasses-app/        # Standalone glasses app (Kotlin/Jetpack Compose)
├── phone-app/          # Future: phone companion for Rokid Max/AR line
├── shared/             # Protocol definitions & data models
└── README.md
```

---

## 📄 License

MIT — build on it, hack it, make it yours.
