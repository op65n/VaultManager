# Vault Manager
Private Player Vaults plugin for 1.16+
<br>
Version: `0.0.9-PreRelease` <br>
Native API: `Paper-1.16.5-R0.1-SNAPSHOT` <br>
Source Code: <a href="https://github.com/op65n/VaultManager">github.com/op65n/VaultManager</a> <br>
Developer: `Frcsty` <br>

## Commands
The plugin provides several User and Admin commands:

- `/privatevault <index/name>` (alias: `pv`) (permission: `/`) <br>
    Opens the users vault at the index/name if they have 
    the permission for it.
  

- `/setvault <index/name> <name>` (alias: `/`) (permission: `vaultmanager.vault.rename`) <br>
    Set's the given vaults new name if it matches the requirements, the user needs access
    to the vault in order to be able to change it.
  

- `/inspectvault <player> <index>` (alias: `iv`) (permission: `vaultmanager.command.admin.inspect`) <br>
    Opens a vault inspector containing the contents of the target's specified vault.
  
    If the executor has the permission `vaultmanager.command.admin.edit` the inspector will allow edits (NOTE: These edits are saved
    so don't do dumb shit.)
  
*
SIZE: vaultmanager.vault.size.<row>
ACCESS: vaultmanager.vault.access.<index>
RANGE: vaultmanager.vault.range.<top index>
