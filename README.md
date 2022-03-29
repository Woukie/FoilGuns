# FoilGuns
## by Lucas Eldon
A project I did earlier this year (an unfinished minecraft java plugin for customisable weapons)
Feel free to learn/modify/reuse code from this, this was my last java edition project before moving to bedrock

I don't even know if this still works and never got round to polishing it, the known features are/were:
- Create guns for specific items
- Specify particle paramaters
- Customise damage
- Bullets reflect of walls given an incident angle + probability
- Specify distance between steps of the bullet (something like a ray is used)
- Hit particles
- Travel particles
- Cast particles
- Number of enimies to pierce before bullet is stopped
- Specify blocks to pass through
- Customise travel distance
- Super slick tab complete
- Bullet casting offset

Bullet is cast from the eyes but an offset can be set, this will warp the particles ONLY so they are not in the face (bullet will still appear to land in same place), it just makes sense to cast from the face (where you are aiming) rather than the hand/whatever you set as the offset

Stuff I know I didn't implement (but wanted to):
- Bullet firing cooldown (I added this but can't remeber if it works or not)
- Reloading
- Ammo consumption
- Firerate
- Accuracy + aiming
- Recoil (likley not possible)
- Saving bullet configurations
- Enable/disable specific bullet configs for use with minigames and such where that would be needed
