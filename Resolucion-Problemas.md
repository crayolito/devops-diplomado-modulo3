# 🔐 Guía Completa: Configurar Azure DevOps con Key Vault

## 📋 Tabla de Contenido

1. [Problema Original](#problema-original)
2. [Prerequisitos](#prerequisitos)
3. [Paso 1: Configurar Permisos de Usuario](#paso-1-configurar-permisos-de-usuario)
4. [Paso 2: Crear Service Principal](#paso-2-crear-service-principal)
5. [Paso 3: Configurar Service Connection](#paso-3-configurar-service-connection)
6. [Paso 4: Configurar Permisos en Key Vault](#paso-4-configurar-permisos-en-key-vault)
7. [Paso 5: Crear Variable Groups](#paso-5-crear-variable-groups)
8. [Troubleshooting](#troubleshooting)
9. [Comandos de Referencia Rápida](#comandos-de-referencia-rápida)

---

## 🎯 Problema Original

**Situación:**

- Usuario invitado (jsahonero@farmacorp.com) necesita ser co-administrador
- Necesita crear Variable Groups en Azure DevOps vinculados a Azure Key Vault
- El usuario no puede ver la suscripción correcta desde Azure DevOps
- Permisos cruzados entre tenants diferentes

**¿Por qué no funcionó solo dar permisos?**

- Azure DevOps usa su propio contexto de autenticación
- Los usuarios invitados de otros tenants no pueden crear Service Connections automáticamente
- Se necesita un Service Principal específico como "puente" entre Azure DevOps y Azure

---

## ✅ Prerequisitos

### Información que necesitarás:

- **Suscripción ID**: `3fed9023-235a-420a-8033-dbac4da1753e`
- **Tenant ID**: `3221682f-2dda-4422-932b-ad9407a1645f`
- **Usuario invitado**: `JSAHONERO_farmacorp.com#EXT#@juancarlosguinchalopezgmail.onmicrosoft.com`
- **Organización Azure DevOps**: `https://dev.azure.com/juancarlosguinchalopez`
- **Proyecto**: `PROYECTO_FINAL`
- **Key Vaults**: `m3-authh-cliente`, `m3-planificacion`, etc.

### Herramientas necesarias:

- Azure CLI instalado y configurado
- Permisos de Owner en la suscripción de Azure
- Acceso como administrador al proyecto de Azure DevOps

---

## 🚀 Paso 1: Configurar Permisos de Usuario

### 1.1 Conectarse como administrador

```powershell
# Asegúrate de estar en la suscripción correcta
az login --tenant "3221682f-2dda-4422-932b-ad9407a1645f"
az account set --subscription "3fed9023-235a-420a-8033-dbac4da1753e"
```

### 1.2 Verificar suscripción actual

```powershell
az account show --output table
```

### 1.3 Otorgar permisos de Owner al usuario invitado

```powershell
az role assignment create \
  --role "Owner" \
  --assignee "JSAHONERO_farmacorp.com#EXT#@juancarlosguinchalopezgmail.onmicrosoft.com" \
  --scope "/subscriptions/3fed9023-235a-420a-8033-dbac4da1753e"
```

### 1.4 Verificar permisos asignados

```powershell
az role assignment list \
  --assignee "JSAHONERO_farmacorp.com#EXT#@juancarlosguinchalopezgmail.onmicrosoft.com" \
  --scope "/subscriptions/3fed9023-235a-420a-8033-dbac4da1753e" \
  --output table
```

**Resultado esperado:**

```
Principal                                                                 Role     Scope
------------------------------------------------------------------------  -------  ---------------------------------------------------
JSAHONERO_farmacorp.com#EXT#@juancarlosguinchalopezgmail.onmicrosoft.com  Owner    /subscriptions/3fed9023-235a-420a-8033-dbac4da1753e
```

---

## 🔧 Paso 2: Crear Service Principal

### ¿Por qué necesitamos un Service Principal?

Un Service Principal actúa como una "identidad de servicio" que Azure DevOps puede usar para autenticarse con Azure, independientemente del usuario que esté logueado.

### 2.1 Crear Service Principal

```powershell
az ad sp create-for-rbac \
  --name "AzureDevOps-KeyVault-SP" \
  --role "Contributor" \
  --scopes "/subscriptions/3fed9023-235a-420a-8033-dbac4da1753e"
```

**⚠️ IMPORTANTE: Guarda esta información de forma segura:**

```json
{
  "appId": "5e6bd25c-e971-4194-bca7-4cd2edffb7e4",
  "displayName": "AzureDevOps-KeyVault-SP",
  "password": "zBw8Q~95GV9wN1kaPFRHRTB282WuMbregP2JsaCa",
  "tenant": "3221682f-2dda-4422-932b-ad9407a1645f"
}
```

### 2.2 Verificar Service Principal creado

```powershell
az ad sp show --id "5e6bd25c-e971-4194-bca7-4cd2edffb7e4" --query "{displayName:displayName, appId:appId}" --output table
```

---

## 🔗 Paso 3: Configurar Service Connection

### 3.1 Acceder a Azure DevOps

1. Ve a: `https://dev.azure.com/juancarlosguinchalopez/PROYECTO_FINAL/_settings/adminservices`
2. Click en **"+ New service connection"**

### 3.2 Seleccionar tipo de conexión

1. Selecciona **"Azure Resource Manager"** → **Next**
2. Selecciona **"App registration or managed identity (manual)"** → **Next**
3. En **"Credential"** selecciona **"Secret"** (NO Workload Identity Federation)

### 3.3 Llenar formulario de configuración

#### Configuración básica:

```
Environment: Azure Cloud
Scope Level: Subscription
Subscription ID: 3fed9023-235a-420a-8033-dbac4da1753e
Subscription name: Azure subscription 1
```

#### Autenticación:

```
Application (client) ID: 5e6bd25c-e971-4194-bca7-4cd2edffb7e4
Directory (tenant) ID: 3221682f-2dda-4422-932b-ad9407a1645f
Client secret: zBw8Q~95GV9wN1kaPFRHRTB282WuMbregP2JsaCa
```

#### Detalles de conexión:

```
Service Connection Name: Azure-KeyVault-Connection
Description: Connection to access Key Vaults for Variable Groups
```

#### Seguridad:

```
✅ Grant access permission to all pipelines
```

### 3.4 Verificar y guardar

1. Click **"Verify and save"**
2. Debería mostrar: ✅ **"Verification succeeded"**

---

## 🗝️ Paso 4: Configurar Permisos en Key Vault

### ¿Por qué este paso?

El Service Principal necesita permisos específicos de "Get" y "List" en cada Key Vault para poder leer los secrets.

### 4.1 Método 1: Azure CLI (Recomendado)

```powershell
# Para un Key Vault específico
az keyvault set-policy \
  --name "m3-authh-cliente" \
  --spn "5e6bd25c-e971-4194-bca7-4cd2edffb7e4" \
  --secret-permissions get list
```

### 4.2 Para todos los Key Vaults a la vez

```powershell
# Lista de Key Vaults
$keyVaults = @(
    "m3-authh-cliente",
    "m3-compras",
    "m3-inventario",
    "m3-planificacion",
    "m3-ventas",
    "frontendG",
    "ventasVaults"
)

# Configurar permisos para todos
foreach ($vault in $keyVaults) {
    az keyvault set-policy --name $vault --spn "5e6bd25c-e971-4194-bca7-4cd2edffb7e4" --secret-permissions get list
    Write-Host "✅ Permisos configurados para: $vault"
}
```

### 4.3 Método 2: PowerShell (Alternativo)

```powershell
# Si Azure DevOps te proporciona este comando
$ErrorActionPreference="Stop"
Login-AzAccount -SubscriptionId 3fed9023-235a-420a-8033-dbac4da1753e
$spn=(Get-AzADServicePrincipal -SPN 5e6bd25c-e971-4194-bca7-4cd2edffb7e4)
Set-AzKeyVaultAccessPolicy -VaultName NOMBRE-KEYVAULT -ObjectId $spn.Id -PermissionsToSecrets get,list
```

### 4.4 Verificar permisos

```powershell
az keyvault show --name "m3-authh-cliente" --query "properties.accessPolicies" --output table
```

---

## 📦 Paso 5: Crear Variable Groups

### 5.1 Crear Variable Group básico via CLI

```powershell
# Configurar Azure DevOps CLI
az extension add --name azure-devops
$env:AZURE_DEVOPS_EXT_PAT = "TU-PERSONAL-ACCESS-TOKEN"

# Crear variable group base
az pipelines variable-group create \
  --name "m3-auth-cliente-variables" \
  --variables temp="placeholder" \
  --organization "https://dev.azure.com/juancarlosguinchalopez" \
  --project "PROYECTO_FINAL"
```

### 5.2 Linkear al Key Vault via Web UI

1. Ve a: `https://dev.azure.com/juancarlosguinchalopez/PROYECTO_FINAL/_library`
2. Click en tu variable group → **"Edit"**
3. Activa: **"Link secrets from an Azure key vault as variables"**
4. Selecciona tu **Service Connection**: `Azure-KeyVault-Connection`
5. Selecciona **Key vault**: `m3-authh-cliente`
6. Click **"+ Add"** y selecciona los secrets necesarios:
   - `api-url-base`
   - `db-name`
   - `db-password`
   - `jwt-expiration`
   - `jwt-secret`
7. Click **"Save"**

### 5.3 Usar Variable Group en Pipeline

```yaml
# En tu pipeline YAML
variables:
  - group: m3-auth-cliente-variables

steps:
  - script: |
      echo "API URL: $(api-url-base)"
      echo "Database: $(db-name)"
      # $(jwt-secret) estará disponible como variable secreta
```

---

## 🚨 Troubleshooting

### Error: "Cannot find user or service principal"

```
Cannot find user or service principal in graph database for 'jsahonero@farmacorp.com'
```

**Solución:** Usar el UserPrincipalName completo:

```
JSAHONERO_farmacorp.com#EXT#@juancarlosguinchalopezgmail.onmicrosoft.com
```

### Error: "doesn't have secrets list permission on key vault"

```
The user does not have secrets list permission on key vault 'm3-authh-cliente'
```

**Solución:** Ejecutar comando de permisos en Key Vault:

```powershell
az keyvault set-policy --name "m3-authh-cliente" --spn "5e6bd25c-e971-4194-bca7-4cd2edffb7e4" --secret-permissions get list
```

### Error: "Service connection already exists"

```
Service connection with name Azure-KeyVault-Connection already exists
```

**Solución:** Usar un nombre diferente:

```
Service Connection Name: m3-auth-keyvault-connection
```

### Error: "Can't find subscription you need"

**Causa:** Usuario en tenant diferente al de la suscripción
**Solución:** Usar Service Principal manual (este tutorial)

### Error: "Project information name is not valid"

**Causa:** JSON malformado en REST API
**Solución:** Usar interfaz web en lugar de REST API

---

## ⚡ Comandos de Referencia Rápida

### Verificación de Estado

```powershell
# Verificar suscripción actual
az account show --output table

# Verificar permisos de usuario
az role assignment list --assignee "USUARIO" --scope "/subscriptions/ID" --output table

# Verificar Service Principal
az ad sp show --id "APP-ID" --query "{displayName:displayName, appId:appId}"

# Verificar permisos Key Vault
az keyvault show --name "KEYVAULT-NAME" --query "properties.accessPolicies"

# Listar Variable Groups
az pipelines variable-group list --organization "ORG-URL" --project "PROJECT" --output table
```

### Cleanup (Si necesitas empezar de nuevo)

```powershell
# Eliminar Service Principal
az ad sp delete --id "5e6bd25c-e971-4194-bca7-4cd2edffb7e4"

# Eliminar Variable Group
az pipelines variable-group delete --group-id ID --organization "ORG-URL" --project "PROJECT" --yes

# Remover permisos Key Vault
az keyvault delete-policy --name "KEYVAULT-NAME" --spn "APP-ID"
```

---

## ✅ Checklist Final

### Antes de empezar:

- [ ] Tengo permisos de Owner en la suscripción Azure
- [ ] Tengo acceso de administrador al proyecto Azure DevOps
- [ ] Tengo Azure CLI instalado y configurado
- [ ] Tengo la información de suscripción, tenant, y Key Vaults

### Proceso completado:

- [ ] Usuario invitado tiene permisos Owner en suscripción
- [ ] Service Principal creado con credenciales guardadas
- [ ] Service Connection configurado y verificado en Azure DevOps
- [ ] Permisos configurados en todos los Key Vaults necesarios
- [ ] Variable Groups creados y vinculados a Key Vaults
- [ ] Prueba exitosa de acceso a secrets desde Azure DevOps

---

## 📝 Notas Importantes

1. **Seguridad:** Nunca compartas las credenciales del Service Principal en código o repositorios
2. **Rotación:** Las credenciales del Service Principal expiran, configura rotación automática
3. **Principio de menor privilegio:** Solo otorga permisos "get" y "list" en Key Vault, no "set" o "delete"
4. **Documentación:** Mantén este README actualizado si cambias configuraciones

---

## 🤝 Colaboradores

- **Administrador:** juan carlos guincha lopez (juancarlosguinchalopez@gmail.com)
- **Usuario invitado:** Jose Alejandro Sahonero Salas (jsahonero@farmacorp.com)

---

## 📚 Referencias

- [Azure DevOps Service Connections](https://docs.microsoft.com/en-us/azure/devops/pipelines/library/service-endpoints)
- [Azure Key Vault Access Policies](https://docs.microsoft.com/en-us/azure/key-vault/general/assign-access-policy)
- [Azure Service Principals](https://docs.microsoft.com/en-us/azure/active-directory/develop/app-objects-and-service-principals)
- [Azure DevOps Variable Groups](https://docs.microsoft.com/en-us/azure/devops/pipelines/library/variable-groups)

---

**Fecha de creación:** 30 de Noviembre, 2025  
**Versión:** 1.0  
**Estado:** ✅ Funcional y probado
