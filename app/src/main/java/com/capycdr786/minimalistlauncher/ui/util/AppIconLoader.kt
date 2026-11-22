package com.capycdr786.minimalistlauncher.ui.util

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.capycdr786.minimalistlauncher.R

object AppIconLoader {

    fun getMinimalIconRes(packageName: String, label: String, activityName: String = ""): Int {
        val lowerPkg = packageName.lowercase()
        val lowerLabel = label.lowercase()
        val lowerActivity = activityName.lowercase()

        fun matches(vararg keywords: String): Boolean {
            return keywords.any {
                lowerPkg.contains(it) || lowerLabel.contains(it) || lowerActivity.contains(it)
            }
        }

        return when {
            matches("vending", "playstore", "play store", "aurora", "fdroid", "f-droid", "neostore", "neo store", "obtainium", "droid-ify", "droidify", "accrescent", "market") ->
                R.drawable.ic_playstore

            matches("bhim", "phonepe", "paytm", "gpay", "google pay", "cred", "paisa", "upi", "mobikwik", "bharatpe", "slice", "freecharge", "payzapp", "navi", "super.money", "supermoney", "wallet", "payment", "bank", "banking", "yono", "imobile", "fedmobile", "kotak", "hdfc", "icici", "sbi", "axis", "canara", "pnb", "indusind") ->
                R.drawable.ic_dollar

            matches("qr", "scanner", "barcode") ->
                R.drawable.ic_qr_code

            matches("swiggy", "zomato", "bigbasket", "bbdaily", "bbnow", "zepto", "blinkit", "instamart", "grofers", "flipkart", "shopsy", "amazon", "myntra", "meesho", "ajio", "nykaa", "jiomart", "dunzo", "tata neu", "tatanew", "croma", "1mg", "apollo", "pharmeasy", "netmeds", "licious", "country delight", "shopping", "grocery", "mart", "supermarket", "ecommerce", "store", "cart") ->
                R.drawable.ic_shopping

            matches("game", "games", "gaming", "play games", "steam", "epic", "roblox", "minecraft", "genshin", "pubg", "codm", "call of duty", "bgmi", "free fire", "clash", "brawl", "chess", "subway", "asphalt", "candy", "fortnite", "emulator", "retroarch", "yuzu", "ppsspp", "dolphin", "sudoku", "crossword", "riot", "league") ->
                R.drawable.ic_game

            matches("futo", "heliboard", "florisboard", "openboard", "keyboard", "inputmethod", "gboard", "swiftkey") ->
                R.drawable.ic_keyboard

            matches("brave", "zen", "mull", "fennec", "cromite", "kiwi", "duckduckgo", "torbrowser", "tor browser", "org.torproject", "bromite", "browser", "chrome", "firefox", "opera", "edge", "internet", "vivaldi") ->
                R.drawable.ic_browser

            matches("morphe", "morph", "newpipe", "revanced", "libretube", "tubular", "vlc", "invidious", "youtube", "video", "netflix", "player", "kodi", "mpv") ->
                R.drawable.ic_video

            matches("spotube", "vimusic", "innertune", "antennapod", "auxio", "retro", "metro", "symfonium", "namida", "musicolet", "music", "spotify", "audio", "sound", "podcast") ->
                R.drawable.ic_music

            matches("simplex", "briar", "element", "matrix", "session", "threema", "signal", "telegram", "whatsapp", "mms", "sms", "message", "chat", "discord", "conversations") ->
                R.drawable.ic_messages

            matches("bitwarden", "keepass", "aegis", "ente", "2fas", "authenticator", "vault", "protonvpn", "wireguard", "orbot", "mullvad", "adguard", "vpn", "shield") ->
                R.drawable.ic_shield

            matches("termux", "terminal", "console", "shell") ->
                R.drawable.ic_terminal

            matches("dialer", "dial", "call", "koler", "truecaller", "telecom", "phone") ->
                R.drawable.ic_phone

            matches("contact", "contacts", "phonebook", "addressbook", "people") ->
                R.drawable.ic_contacts

            matches("refra", "openn", "opengallery", "open gallery", "aves", "gallery", "gallary", "photo", "image", "picture") ->
                R.drawable.ic_gallery

            matches("camera", "gcam", "opencamera") ->
                R.drawable.ic_camera

            matches("settings", "setting", "preference", "config") ->
                R.drawable.ic_settings

            matches("fairemail", "k9", "k-9", "thunderbird", "tuta", "mail", "email", "gmail", "outlook") ->
                R.drawable.ic_mail

            matches("calendar", "agenda") ->
                R.drawable.ic_calendar

            matches("clock", "alarm", "timer", "watch") ->
                R.drawable.ic_clock

            matches("materialfiles", "mixplorer", "file", "document", "download", "explorer") ->
                R.drawable.ic_files

            matches("obsidian", "logseq", "standardnotes", "markor", "joplin", "simplenote", "note", "keep", "memo", "todo") ->
                R.drawable.ic_notes

            matches("calc", "calculator") ->
                R.drawable.ic_calculator

            matches("osmand", "organicmaps", "map", "navigation", "waze", "gps") ->
                R.drawable.ic_maps

            else -> R.drawable.ic_default_app
        }
    }

    val availableIcons: List<SelectableIcon> = listOf(
        SelectableIcon("phone", "Phone", R.drawable.ic_phone),
        SelectableIcon("contacts", "Contacts", R.drawable.ic_contacts),
        SelectableIcon("messages", "Messages", R.drawable.ic_messages),
        SelectableIcon("mail", "Email", R.drawable.ic_mail),
        SelectableIcon("dollar", "Banking / UPI", R.drawable.ic_dollar),
        SelectableIcon("shopping", "Shopping", R.drawable.ic_shopping),
        SelectableIcon("qr_code", "QR Code", R.drawable.ic_qr_code),
        SelectableIcon("browser", "Browser", R.drawable.ic_browser),
        SelectableIcon("camera", "Camera", R.drawable.ic_camera),
        SelectableIcon("gallery", "Gallery", R.drawable.ic_gallery),
        SelectableIcon("video", "Video", R.drawable.ic_video),
        SelectableIcon("music", "Music", R.drawable.ic_music),
        SelectableIcon("playstore", "Store", R.drawable.ic_playstore),
        SelectableIcon("notes", "Notes", R.drawable.ic_notes),
        SelectableIcon("calendar", "Calendar", R.drawable.ic_calendar),
        SelectableIcon("clock", "Clock", R.drawable.ic_clock),
        SelectableIcon("calculator", "Calculator", R.drawable.ic_calculator),
        SelectableIcon("files", "Files", R.drawable.ic_files),
        SelectableIcon("maps", "Maps", R.drawable.ic_maps),
        SelectableIcon("settings", "Settings", R.drawable.ic_settings),
        SelectableIcon("shield", "Security", R.drawable.ic_shield),
        SelectableIcon("game", "Games", R.drawable.ic_game),
        SelectableIcon("terminal", "Terminal", R.drawable.ic_terminal),
        SelectableIcon("keyboard", "Keyboard", R.drawable.ic_keyboard),
        SelectableIcon("default_app", "Default", R.drawable.ic_default_app),
    )

    fun getIconResById(id: String?): Int? {
        if (id.isNullOrBlank()) return null
        return availableIcons.firstOrNull { it.id == id }?.resId
    }
}

data class SelectableIcon(
    val id: String,
    val name: String,
    val resId: Int,
)

@Composable
fun AppIcon(
    packageName: String,
    label: String,
    activityName: String = "",
    customIconId: String? = null,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
) {
    val iconRes = AppIconLoader.getIconResById(customIconId)
        ?: AppIconLoader.getMinimalIconRes(packageName, label, activityName)
    Icon(
        painter = painterResource(id = iconRes),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onBackground,
        modifier = modifier.size(size),
    )
}
