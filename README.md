# Helix Client

This repository keeps both client targets side by side:

- `fabric/` - modern Fabric client MVP for Minecraft 1.21.11.
- `forge-1.8.9/` - Forge client MVP for Minecraft 1.8.9.

## Build Fabric

```powershell
cd fabric
.\gradlew.bat build
```

Output:

```plain text
fabric/build/libs/HelixClient-Fabric.jar
```

## Build Forge 1.8.9

Forge 1.8.9 should be built with Java 8.

```powershell
cd forge-1.8.9
.\gradlew.bat build
```

Output:

```plain text
forge-1.8.9/build/libs/HelixClient-1.8.9.jar
```
