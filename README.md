# 🥽 Rokid OpenClaw

**Rokid AI Glasses + OpenClaw integration — voice-first wearable AI**

Connect your Rokid AR glasses to [OpenClaw](https://openclaw.ai) for a hands-free, voice-first AI assistant experience. Talk to Claude, capture your world through the glasses camera, and get AI responses displayed right on your HUD.

---

## ✨ Planned Features

| Feature | Description |
|---------|-------------|
| 🎤 **Voice Input** | Speak naturally — audio captured from phone mic, transcribed and sent to OpenClaw |
| 📸 **Camera Capture** | Grab frames from the Rokid camera and send to Claude's vision API |
| 🖥️ **HUD Display** | AI responses rendered directly on the Rokid glasses overlay |
| 🔊 **TTS Responses** | Claude speaks back via OpenClaw TTS, played through phone speaker |
| 🔗 **Session Management** | Persistent conversation sessions via OpenClaw Gateway |
| 📡 **Wake Word** | Always-on listening with configurable wake word |
| 🗺️ **Context Awareness** | Location, time, and sensor data piped into prompts |

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        Rokid Glasses                            │
│  Camera ──► Video Stream ──► Phone App                          │
│  Display ◄─ HUD Overlay  ◄─ Phone App                          │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │  USB / WiFi
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                      Phone App (Android)                        │
│  • Audio capture & VAD (voice activity detection)               │
│  • Rokid CXR SDK integration                                    │
│  • OpenClaw Gateway client                                      │
│  • TTS playback                                                 │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │  HTTP / WebSocket
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                     OpenClaw Gateway                            │
│  • Claude API (Anthropic)                                       │
│  • Tool execution (web search, memory, etc.)                    │
│  • Session persistence                                          │
└─────────────────────────────────────────────────────────────────┘
```

**Two-component app:**
- **`phone-app/`** — Android companion app (Kotlin). Handles audio, camera relay, OpenClaw comms, TTS.
- **`glasses-app/`** — Rokid HUD overlay app. Displays AI responses on the AR display.
- **`shared/`** — Protocol definitions and data models shared between components.

---

## 🙏 Inspiration

This project is directly inspired by **[Clawsses](https://github.com/dweddepohl/clawsses)** by [@dweddepohl](https://github.com/dweddepohl) — a Claude-powered smart glasses app. Clawsses proved the concept beautifully; Rokid OpenClaw adapts it for the Rokid platform with OpenClaw's persistent session architecture.

Go give Clawsses a ⭐!

---

## 📋 Requirements

- [Android Studio](https://developer.android.com/studio) (latest stable)
- [Rokid CXR SDK](https://developer.rokid.com/) — AR glasses integration SDK
- [OpenClaw Gateway](https://openclaw.ai) — running locally or remote
- Rokid Max / Max Pro glasses (or compatible model)
- Android phone (API 26+)

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
2. Open `phone-app/` in Android Studio
3. Configure your OpenClaw Gateway URL in `phone-app/src/main/res/values/config.xml`
4. Build and deploy to your Android phone
5. Pair your Rokid glasses
6. Say the wake word and start chatting!

---

## 📁 Project Structure

```
rokid-openclaw/
├── phone-app/          # Android companion app (Kotlin)
├── glasses-app/        # Rokid HUD overlay app
├── shared/             # Protocol definitions & data models
└── README.md
```

---

## 📄 License

MIT — build on it, hack it, make it yours.
