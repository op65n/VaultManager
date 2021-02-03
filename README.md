# Vault Manager
Private Player Vaults plugin for 1.16+
<br>
Version: `0.0.1-Alpha` <br>
Native API: `Paper-1.16.4-R0.1-SNAPSHOT` <br>
Source Code: <a href="https://github.com/op65n/VaultManager">github.com/op65n/VaultManager</a> <br>
Developer: `Frcsty` <br>

## How To (Server Owner)
This is a plugin built on PaperAPI, and is required to properly run this plugin.

<b>Installation:</b> <br>
| Place VaultManager.jar (`VaultManager.jar`) file into the plugins folder. <br>
| Start the server, plugin will generate `VaultManager` directory with files:
* `config.yml`
* `storage/` (directory containing user's vault file) <br>

| Stop the server after everything has been loaded. <br>
| Open and configure the plugin to your needs. <br>
| Start the server and enjoy the plugin!

## Commands
The plugin provides several User and Admin commands:

- `/privatevault <index/name>` (Alias: `pv`) <br>
    Opens the users vault at the index/name if they have 
    the permission for it
