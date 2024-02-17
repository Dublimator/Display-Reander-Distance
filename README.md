Certainly, here's the modified version without the last three sections:

---

# DispalyRenderDistance Plugin

**Version:** 0.0.1  
**Author:** Dublimator

---

## Description

The DispalyRenderDistance plugin is designed to offer flexible rendering distance settings for item displays in your Minecraft server. It allows users to customize up to 9 variants of rendering distances, giving players the freedom to choose the one that best suits their needs through commands such as `/drd` or the `/DRDistance menu`.

## Features

- Customize rendering distances for item displays.
- Supports up to 9 distance variants.
- Easy configuration via commands.
- Seamless integration with your Minecraft server.
  
## Installation

1. **Download:**
    - Download the latest release from the [Releases](https://github.com/Dublimator/Display-Render-Distance/releases) section.
2. **Installation:**
    - Place the downloaded `.jar` file into your server's `plugins` directory.
3. **Restart:**
    - Restart or reload your server to enable the plugin.

## Usage

1. **Setting Rendering Distance:**
    - Use the `/drd` command to configure the rendering distance settings.
2. **DRDistance Menu:**
    - Alternatively, players can utilize the DRDistance menu to select their preferred rendering distance.
3. **Reloading the Plugin:**
    - Use `/DRDistance reload` to reload the plugin and apply new configurations.

## Commands

- `/drd [distance]`: Set the rendering distance.
- `/drd help`: Display help for using the plugin.
- `/DRDistance reload`: Reload the plugin and apply new configurations.

## Permissions

- `drd.use`: Allows players to use DispalyRenderDistance commands.
- `drd.admin`: Grants administrative access to DispalyRenderDistance plugin features.

## Configuration

The plugin's configuration file (`config.yml`) allows further customization options.

```yaml
mySQL:
  database: ""
  host: ""
  user: ""
  password: ""

menu-name: "Display Optimization"

# Entity render distance (server)
server-max-distance: 100

# How often displays around the player are checked in ticks (May affect server load)
update-displays: 3

# Button click sound for the menu
button-sound: BLOCK_NOTE_BLOCK_BANJO

# Block rendering distance in blocks
# slot: 0-8
# Use distance=0 to disable all displays and distance=-1 to show
# Use custom-model-data=0 to prevent the item from receiving its value
# You can add from 1 to 9 buttons
buttons:
  all-off:
    distance: 0
    slot: 1
    item: PAPER
    custom-model-data: 1
    button-name: "all-off"
    button-lore:
      - "all-off"

  distance-low:
    distance: 10
    slot: 3
    item: PAPER
    custom-model-data: 1
    button-name: "Low"
    button-lore:
      - "Low"

  distance-medium:
    distance: 20
    slot: 4
    item: PAPER
    custom-model-data: 1
    button-name: "Medium"
    button-lore:
      - "Medium"

  distance-high:
    distance: 30
    slot: 5
    item: PAPER
    custom-model-data: 1
    button-name: "High"
    button-lore:
      - "High"

  all-on:
    distance: -1
    slot: 7
    item: PAPER
    custom-model-data: 1
    button-name: "all-on"
    button-lore:
      - "all-on"

```

---
