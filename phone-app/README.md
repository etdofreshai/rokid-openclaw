# 📱 Phone App (Future)

This directory is reserved for a **future phone companion app** for the Rokid Max/AR glasses line.

## Why not needed now?

The **Rokid AI Glasses** connect directly to OpenClaw Gateway over WiFi — no phone bridge required. The glasses run Android internally and can run standalone Jetpack Compose apps.

## When would this be needed?

The **Rokid Max / AR line** requires the [CXR SDK](https://developer.rokid.com/) and an Android phone as a bridge between the glasses and external services. For that hardware, you'd need:

- An Android phone running this companion app
- CXR SDK integration for glasses display/camera relay
- The phone app would handle WiFi/WebSocket communication to OpenClaw Gateway

## Reference

The **[Clawsses](https://github.com/dweddepohl/clawsses)** project by [@dweddepohl](https://github.com/dweddepohl) demonstrates the phone-bridge approach for smart glasses. Check it out for inspiration if building this component.
