# Anhpan's Morphe Patches

Personal Morphe Patches for Zalo.

## About

Custom patches for Zalo, including Anhpan's settings.

### How to use these patches

Click the button below, or manually add this repo url to Morphe Manager: https://github.com/rustybrozen/anhpan-patches

<a href="https://morphe.software/add-source?github=rustybrozen/anhpan-patches">
  <img src=".github/assets/add-to-morphe.svg" alt="Add to Morphe" height="48">
</a>

## Patches list

<!-- PATCHES_START -->
> **[v1.1.0-dev.12](https://github.com/rustybrozen/anhpan-patches/releases/tag/v1.1.0-dev.12)**&nbsp;&nbsp;&middot;&nbsp;&nbsp;`dev`&nbsp;&nbsp;&middot;&nbsp;&nbsp;**1 patch** across **1 app**&nbsp;&nbsp;&middot;&nbsp;&nbsp;[Full details](PATCHES.md)

| # | App | Patches | Version | Package |
|---|---|---|---|---|
| 1 | [**Zalo**](PATCHES.md#zalo-comzingzalo) | 1 | `26.11.01` | [`com.zing.zalo`](https://play.google.com/store/apps/details?id=com.zing.zalo) |
<!-- PATCHES_END -->

### Building locally

- Run `./gradlew buildAndroid`
- The built patches .mpp file is found in `patches/build/libs/patches-*.mpp`
- Patch the mpp file using [Morphe-Desktop](https://github.com/MorpheApp/morphe-desktop)
  like any other patch bundle.

See the [Morphe documentation](https://github.com/MorpheApp/morphe-documentation) for more information.

## License

Anhpan's Morphe Patches are licensed under the [GNU General Public License v3.0](LICENSE)
