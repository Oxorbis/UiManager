package cz.creeperface.hytale.uimanager.templates

import cz.creeperface.hytale.uimanager.type.buttonSounds
import cz.creeperface.hytale.uimanager.type.dropdownBoxSounds
import cz.creeperface.hytale.uimanager.type.soundStyle

object SoundsTemplate {
    val buttonsMainActivate = CommonTemplate.UI_ROOT + "Sounds/ButtonsMainActivate.ogg"
    val buttonsMainHover = CommonTemplate.UI_ROOT +  "Sounds/ButtonsMainHover.ogg"

    val buttonsLightActivate = CommonTemplate.UI_ROOT + "Sounds/ButtonsLightActivate.ogg"
    val buttonsLightHover = CommonTemplate.UI_ROOT + "Sounds/ButtonsLightHover.ogg"

    val buttonsCancelActivate = CommonTemplate.UI_ROOT + "Sounds/ButtonsCancelActivate.ogg"

    val cosmeticsTilesActivate = CommonTemplate.UI_ROOT + "Sounds/CosmeticsTilesActivate.ogg"

    val enterWorldActivate = CommonTemplate.UI_ROOT + "Sounds/EnterWorldActivate.ogg"

    val saveActivate = CommonTemplate.UI_ROOT + "Sounds/SaveActivate.ogg"

    val respawn = CommonTemplate.UI_ROOT + "Sounds/Respawn_Stereo.ogg"

    val shuffleActivate = CommonTemplate.UI_ROOT + "Sounds/ShuffleActivate.ogg"

    val tick = CommonTemplate.UI_ROOT + "Sounds/TickActivate.ogg"

    val untick = CommonTemplate.UI_ROOT + "Sounds/UntickActivate.ogg"

    val topBarActivate = CommonTemplate.UI_ROOT + "Sounds/TopBarActivate.ogg"

    val hButtonHover = CommonTemplate.UI_ROOT + "Sounds/HButtonHover.ogg"

    val buttonsMain = buttonSounds {
        activate = soundStyle {
            soundPath = buttonsMainActivate
            volume = 6.0
        }

        mouseHover = soundStyle {
            soundPath = buttonsMainHover
            minPitch = -0.1
            maxPitch = 0.1
            volume = 2.0
        }
    }

    val buttonsLight = buttonSounds {
        activate = soundStyle {
            soundPath = buttonsLightActivate
            minPitch = -0.4
            maxPitch = 0.4
            volume = 4.0
        }
        mouseHover = soundStyle {
            soundPath = buttonsLightHover
            volume = 6.0
        }
    }

    val buttonsCancel = buttonSounds {
        activate = soundStyle {
            soundPath = buttonsCancelActivate
            minPitch = -0.4
            maxPitch = 0.4
            volume = 6.0
        }
        mouseHover = soundStyle {
            soundPath = buttonsLightHover
            volume = 6.0
        }
    }

    val cosmeticsTiles = buttonSounds {
        activate = soundStyle {
            soundPath = cosmeticsTilesActivate
            minPitch = -0.4
            maxPitch = 0.4
            volume = 6.0
        }
        mouseHover = soundStyle {
            soundPath = buttonsLightHover
            volume = 6.0
        }
    }

    val dropdownBox = dropdownBoxSounds {
        activate = soundStyle {
            soundPath = tick
            volume = 6.0
        }
        mouseHover = soundStyle {
            soundPath = buttonsLightHover
            volume = 6.0
        }
        close = soundStyle {
            soundPath = buttonsCancelActivate
            volume = 6.0
        }
    }

    val enterWorld = buttonSounds {
        activate = soundStyle {
            soundPath = enterWorldActivate
            volume = 6.0
        }
        mouseHover = soundStyle {
            soundPath = buttonsLightHover
            volume = 6.0
        }
        context = soundStyle {
            soundPath = buttonsLightActivate
            volume = 6.0
        }
    }

    val lock = buttonSounds {
        activate = soundStyle {
            soundPath = CommonTemplate.UI_ROOT + "Sounds/LockActivate.ogg"
            volume = 6.0
        }
    }

    val unlock = buttonSounds {
        activate = soundStyle {
            soundPath = CommonTemplate.UI_ROOT + "Sounds/UnlockActivate.ogg"
            volume = 6.0
        }
    }

    val saveSettings = buttonSounds {
        activate = soundStyle {
            soundPath = saveActivate
            volume = 6.0
        }
        mouseHover = soundStyle {
            soundPath = buttonsLightHover
            volume = 6.0
        }
    }

    val respawnActivate = buttonSounds {
        activate = soundStyle {
            soundPath = respawn
            volume = -6.0
        }
    }

    val serverList = buttonSounds {
        activate = soundStyle {
            soundPath = enterWorldActivate
            volume = 6.0
        }
        mouseHover = soundStyle {
            soundPath = buttonsLightHover
            volume = 6.0
        }
    }

    val friendsList = buttonSounds { }

    val shuffle = buttonSounds {
        activate = soundStyle {
            soundPath = shuffleActivate
            minPitch = -0.4
            maxPitch = 0.4
            volume = 1.0
        }
    }

    val topBar = buttonSounds {
        activate = soundStyle {
            soundPath = topBarActivate
            volume = 6.0
        }
        mouseHover = soundStyle {
            soundPath = buttonsMainHover
            volume = 2.0
        }
    }
}