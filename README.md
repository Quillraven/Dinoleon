[![Kotlin](https://img.shields.io/badge/Kotlin-1.6.21-red.svg)](http://kotlinlang.org/)
[![Fleks](https://img.shields.io/badge/Fleks-1.3--JVM-success.svg)](https://github.com/Quillraven/Fleks/)

# Dinoleon

Dinoleon is an example game using [Fleks](https://github.com/Quillraven/Fleks) entity component system in
a [LibGDX](https://github.com/libgdx/libgdx) game.

![image](https://user-images.githubusercontent.com/93260/151590713-8411f61a-cdf1-40a7-842a-55f2ee29ffe7.png)

It is a very small and simple game where the player controls a Dinosaur that can switch colors like a Chameleon. There
are walls spawning periodically from the right edge of the screen and as a player you need to switch the color of your
dino within time to not get hit by the wall. If you get hit five times, you lose. There are three different difficulties
which increase the number of spawns, how fast they spawn and also how fast they move.

![image](https://user-images.githubusercontent.com/93260/151590945-6558f01e-8006-402d-9f28-4e75bfc91564.png)

The controls are:

- Mouse for any UI menu related buttons
- QWER to change colors
  - Q -> red
  - W -> green
  - E -> blue
  - R -> orange

![image](https://user-images.githubusercontent.com/93260/151591526-e6aa601d-b89d-4122-9716-2dffaec07f9d.png)

#### The main purpose of Dinoleon is to show how [Fleks](https://github.com/Quillraven/Fleks) can be used. Do not use it as a best practice for game development!

Like e.g., It is total nonsense to use a physic engine like [Box2D](https://box2d.org/) for a simple game like that but
in order to challenge Fleks and to see its capabilities I used it in this game.

### Highlights

- **RenderSystem** as a sorted IteratingSystem
- **PhysicSystem** as a fixed interval IteratingSystem and ContactListener for [Box2D](https://box2d.org/)
- **ScenerySystem** as an Intervalsystem
- **PhysicComponent** with a ComponentListener to properly cleanup physic related resources

You can find system code in the `system` package and component code in the `component` package.

## Credits

- Arks: [Dino Sprites](https://arks.itch.io/dino-characters)
- Szadi art: [Platformer Fantasy Free Package](https://szadiart.itch.io/paltformer-fantasy-complete)
- craftpix.net: [Jungle Cartoon GUI](https://free-game-assets.itch.io/free-jungle-cartoon-gui)
- [Soup of Justice Font](https://www.dafont.com/soup-of-justice.font)
- [cooltext.com](https://de.cooltext.com/) for Logo Generator
- Tim Beek: [Music](https://timbeek.itch.io/royalty-free-music-pack-volume-2)
- FallenBlood: [Sfx](https://fallenblood.itch.io/50-sfx)
- Imagemagick to split images. Example:

  ```magick convert dino_blue.png -crop 24x24 game/dino_blue/frame_%02d.png```
- [GDX-Texture-Packer-GUI](https://github.com/crashinvaders/gdx-texture-packer-gui)
- [GDX-Liftoff](https://github.com/tommyettinger/gdx-liftoff) for basic project setup
