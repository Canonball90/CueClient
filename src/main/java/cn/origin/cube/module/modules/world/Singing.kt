package cn.origin.cube.module.modules.world

import cn.origin.cube.module.Category
import cn.origin.cube.module.Module
import cn.origin.cube.module.ModuleInfo
import cn.origin.cube.settings.IntegerSetting
import cn.origin.cube.settings.ModeSetting
import cn.origin.cube.utils.Timer

@ModuleInfo(name = "Singing", descriptions = "Sing", category = Category.WORLD)
class Singing: Module() {
    val delay: IntegerSetting = registerSetting("Delay", 3, 0, 5);
    var song: ModeSetting<Songs> = registerSetting("Song", Songs.HereWithMe)
    var timer: Timer = Timer()
    var messageCount = 1

    override fun onUpdate() {
        if(song.value == Songs.HereWithMe) {
            if (messageCount == 1) {
                mc.player.sendChatMessage("Watch the sunrise along the coast")
                ++messageCount
                timer.reset()
                return
            }
            if (messageCount == 2 && timer.passedS(delay.value.toDouble())) {
                mc.player.sendChatMessage("As we're both getting old")
                ++messageCount
                timer.reset()
                return
            }
            if (messageCount == 3 && timer.passedS(delay.value.toDouble())) {
                mc.player.sendChatMessage("I can't describe what I'm feeling")
                ++messageCount
                timer.reset()
                return
            }
            if (messageCount == 4 && timer.passedS(delay.value.toDouble())) {
                mc.player.sendChatMessage("And all I know is we're going home")
                ++messageCount
                timer.reset()
                return
            }
            if (messageCount == 5 && timer.passedS(delay.value.toDouble())) {
                mc.player.sendChatMessage("So please don't let me go, oh")
                ++messageCount
                timer.reset()
                return
            }
            if (messageCount == 6 && timer.passedS(delay.value.toDouble())) {
                mc.player.sendChatMessage("Don't let me go, oh-oh-oh")
                ++messageCount
                timer.reset()
                return
            }
            if (messageCount == 7 && timer.passedS(delay.value.toDouble())) {
                mc.player.sendChatMessage("And if it's right")
                ++messageCount
                timer.reset()
                return
            }
            if (messageCount == 8 && timer.passedS(delay.value.toDouble())) {
                mc.player.sendChatMessage("I don't care how long it takes")
                ++messageCount
                timer.reset()
                return
            }
            if (messageCount == 9 && timer.passedS(delay.value.toDouble())) {
                mc.player.sendChatMessage("As long as I'm with you")
                ++messageCount
                timer.reset()
                return
            }
            if (messageCount == 10 && timer.passedS(delay.value.toDouble())) {
                mc.player.sendChatMessage("I've got a smile on my face")
                ++messageCount
                timer.reset()
                return
            }
            if (messageCount == 11 && timer.passedS(delay.value.toDouble())) {
                mc.player.sendChatMessage("Save your tears, it'll be okay")
                ++messageCount
                timer.reset()
                return
            }
            if (messageCount == 12 && timer.passedS(delay.value.toDouble())) {
                mc.player.sendChatMessage("All I know is you're here with me")
                ++messageCount
                timer.reset()
                return
            }

            if (messageCount == 13) {
                mc.player.sendChatMessage("Watch the sunrise as we're getting old, oh-oh")
                ++messageCount
                timer.reset()
                return
            }
            if (messageCount == 14 && timer.passedS(delay.value.toDouble())) {
                mc.player.sendChatMessage("I can't describe, whoa-oh")
                ++messageCount
                timer.reset()
                return
            }
            if (messageCount == 15 && timer.passedS(delay.value.toDouble())) {
                mc.player.sendChatMessage("I wish I could live through every memory again")
                ++messageCount
                timer.reset()
                return
            }
            if (messageCount == 16 && timer.passedS(delay.value.toDouble())) {
                mc.player.sendChatMessage("Just one more time before we float off in the wind")
                ++messageCount
                timer.reset()
                return
            }
            if (messageCount == 17 && timer.passedS(delay.value.toDouble())) {
                mc.player.sendChatMessage("And all the time we spent")
                ++messageCount
                timer.reset()
                return
            }
            if (messageCount == 18 && timer.passedS(delay.value.toDouble())) {
                mc.player.sendChatMessage("Waiting for the light to take us in")
                ++messageCount
                timer.reset()
                return
            }
            if (messageCount == 19 && timer.passedS(delay.value.toDouble())) {
                mc.player.sendChatMessage("AHave been the greatest moments of my life")
                ++messageCount
                timer.reset()
                return
            }
            if (messageCount == 20 && timer.passedS(delay.value.toDouble())) {
                mc.player.sendChatMessage("I don't care how long it takes")
                ++messageCount
                timer.reset()
                return
            }
            if (messageCount == 21 && timer.passedS(delay.value.toDouble())) {
                mc.player.sendChatMessage("As long as I'm with you, I've got a smile on my face")
                ++messageCount
                timer.reset()
                return
            }
            if (messageCount == 22 && timer.passedS(delay.value.toDouble())) {
                mc.player.sendChatMessage("Save your tears, it'll be okay, ay, ay, ay, ay, ay-ay-ay-ay")
                ++messageCount
                timer.reset()
                return
            }
            if (messageCount == 23 && timer.passedS(delay.value.toDouble())) {
                mc.player.sendChatMessage("Yeah, if with me")
                ++messageCount
                timer.reset()
                return
            }
            if (messageCount == 24 && timer.passedS(delay.value.toDouble())) {
                mc.player.sendChatMessage("Oh, oh, oh, oh-oh-oh-oh-oh")
                ++messageCount
                timer.reset()
                return
            }
            if (messageCount == 25 && timer.passedS(delay.value.toDouble())) {
                mc.player.sendChatMessage("I can't describe, oh, oh")
                messageCount = 0
                ++messageCount
                timer.reset()
                return
            }
        }
        if(song.value == Songs.MondayToSonday){
                if (messageCount == 1) {
                    mc.player.sendChatMessage("Caught him in the Beamer, and I had to Tupac him")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 2 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("I'ma pull up wit' a Glock, wristwatch, I'ma rock 'em")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 3 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("Aye, skrt up in the coupe, yeah, I fucked around and shot 'em")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 4 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("Yuh bitch, I hit a lick, Slick Rick, yeah, I got 'em")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 5 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("Ayy, caught him in the Beamer, and I had to Tupac him")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 6 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("I'ma pull up wit' a Glock, wristwatch, I'ma rock 'em")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 7 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("Aye, skrt up in the coupe, yeah, I fucked around and shot 'em")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 8 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("Yuh bitch, I hit a lick, Slick Rick, yeah, I got 'em, ayy")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 9 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("Gang shit, I love the way she buss it down")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 10 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("And I'm fucking with the way she wanna come around")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 11 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("Got a nine millimeter in the fucking round")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 12 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("Better hide when I pull up with the fucking round")
                    ++messageCount
                    timer.reset()
                    return
                }

                if (messageCount == 13) {
                    mc.player.sendChatMessage("Gang, gang, gon' pull up and bang, bang")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 14 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("Ayy mayne, do the race like I'm Tay K")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 15 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("Bang, bang make a tape like I'm Ray J")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 16 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("Melee, stay faded the same day")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 17 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("Rockin' all black air force ones bitch")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 18 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("I turn nothing into something")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 19 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("I love the way yo' girl suck it")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 20 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("Geekin' off some Robitussin")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 21 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("Caught him in the Beamer, and I had to Tupac him")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 22 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("I'ma pull up wit' a Glock, wristwatch, I'ma rock 'em")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 23 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("Aye, skrt up in the coupe, yeah, I fucked around and shot 'em")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 24 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("Yuh bitch, I hit a lick, Slick Rick, yeah, I got 'em")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 25 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("Ayy, caught him in the Beamer, and I had to Tupac him")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 26 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("I'ma pull up wit' a Glock, wristwatch, I'ma rock 'em")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 27 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("Aye, skrt up in the coupe, yeah, I fucked around and shot 'em")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 28 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("Yuh bitch, I hit a lick, Slick Rick, yeah, I got 'em, ayy")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 29 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("Pull up to his hood and I'ma fade 'em up")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 30 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("Pull up on his shorty, I'ma bang her out")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 31 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("Gang in the cut smoking indica")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 32 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("On my reckless shit, Super Saiyan now")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 33 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("Pop pills, I'ma pull up to Fox Hills")
                    ++messageCount
                    timer.reset()
                    return
                }
                if (messageCount == 34 && timer.passedS(delay.value.toDouble())) {
                    mc.player.sendChatMessage("And rock ill, Docs I'm a lot to not kill")
                    messageCount = 0
                    ++messageCount
                    timer.reset()
                    return
                }
        }
    }

    enum class Songs{
        HereWithMe, MondayToSonday
    }
}