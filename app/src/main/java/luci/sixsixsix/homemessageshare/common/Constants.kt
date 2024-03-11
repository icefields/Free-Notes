/**
 * Copyright (C) 2024  Antonio Tari
 *
 * This file is a part of Libre Notes
 * Android self-hosting, note-taking, client + server application
 * @author Antonio Tari
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package luci.sixsixsix.homemessageshare.common

// const val OFFLINE_USERNAME =
// "freenotes-debug-user.random*maybe.unique.id.to.fetch-default-notes"

object Constants {
    const val COLLECTION_LOCALHOST_NAME = "Offline Notes"
    const val COLLECTION_LOCALHOST_ADDRESS = "localhost"
    const val DEFAULT_SERVER = "http://192.168.1.100/freenotes/"
    const val errorStr = "something went wrong"
    const val DB_LOCAL_NAME = "librenotes.db"

    const val THEME_DARK = "Dark"
    const val THEME_DARK_YOU = "Dark_You"
    const val COLOUR_NOTE_DEFAULT = "#008080"
    const val COLOUR_NOTE_OFFLINE = "#0000FF"
    const val THEME_NOTE_DEFAULT = THEME_DARK

    // DONATION LINKS
    const val DONATION_BITCOIN_ADDRESS = "bc1qm9dvdrukgrqpg5f7466u4cy7tfvwcsc8pqshl4"
    const val DONATION_BITCOIN_URI = "bitcoin:$DONATION_BITCOIN_ADDRESS"
    const val DONATION_PAYPAL_URI = "https://paypal.me/powerampache"
}
