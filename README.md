# react-native-privacy-protect

Bun + Turbo monorepo: kütüphane ve örnek uygulama.

## Klasör yapısı

```
react-native-privacy-protect/
├── package.json          # Root: workspaces, turbo scriptleri
├── turbo.json            # Turbo pipeline (build, typecheck, lint, test)
├── bun.lock
├── package/               # Workspace: @enginnblt/react-native-privacy-protect
│   ├── src/
│   ├── android/
│   ├── ios/
│   ├── package.json
│   └── ...
└── example/               # Workspace: örnek RN uygulaması (oluşturulacak)
    ├── package.json       # "workspace:*" ile package'a bağlı
    └── ...
```

Example oluşturduktan sonra root `package.json` içinde `workspaces`'i güncelle: `["package", "example"]`.

## Kurulum

```bash
bun install
```

Tüm workspace’lerin bağımlılıkları yüklenir.

## Scriptler (root’tan)

| Komut | Açıklama |
|--------|-----------|
| `bun run build` | Kütüphaneyi build eder (bob) |
| `bun run build:android` | Example uygulamasının Android build’i |
| `bun run build:ios` | Example uygulamasının iOS build’i |
| `bun run typecheck` | Tüm workspace’lerde typecheck |
| `bun run lint` | Tüm workspace’lerde lint |
| `bun run test` | Tüm workspace’lerde test |
| `bun run clean` | Build/cache temizliği |

## Example workspace

Example oluşturulduktan sonra:

- `example/package.json` içinde: `"@enginnblt/react-native-privacy-protect": "workspace:*"`
- Root’tan: `bun run build:android` veya `bun run build:ios`
- Sadece example’da script çalıştırmak: `bun run start --filter example` (example’da `start` script’i tanımlı olmalı)

## Pre-commit (Husky)

`bun install` sonrası Husky otomatik kurulur. Commit atmadan önce CI ile aynı kontroller çalışır:

- `bun run lint`
- `bun run typecheck`
- `bun run test`

Bunlardan biri hata verirse commit tamamlanmaz. Atlamak için: `git commit --no-verify` (önerilmez).

## Tek workspace’te çalıştırmak

```bash
bun run build --filter package
bun run typecheck --filter package
```
