/*
MIT License
-----------

Copyright (c) 2019 Ivan Yurkov (MB "Stylo tymas" http://steel-team.net)
Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated documentation
files (the "Software"), to deal in the Software without
restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.
*/
package com.steelteam.openrhynn.tickers;

import com.steelteam.openrhynn.data.ServerConfig;
import com.steelteam.openrhynn.entities.Character;
import com.steelteam.openrhynn.network.messages.server.GameCharacterIncreaseVitality;
import com.steelteam.openrhynn.tickers.core.Ticker;
import com.steelteam.openrhynn.utilits.Time;

import java.util.Collection;

public class VitalityRestoreTicker extends Ticker {
    @Override
    public void tick(int threadIndex) {
        long currentTime = Time.getUnixTimeMillis();
        Collection<Character> characters = Character.characters.values();
        for (Character character : characters) {
            if(!character.dead &&
               !character.connectedClient.worldFlagLocked &&
                character.lastReceivedDamageTime + ServerConfig.vitalityIncreaseDamageWaitTime < currentTime &&
                character.lastVitalityIncrease + ServerConfig.vitalityIncrease < currentTime
              ) {
                character.lastVitalityIncrease = currentTime;

                int maxHealthRegenerate = character.connectedModel.healthregenerateBase + character.connectedModel.healthregenerateEffectsExtra;
                int maxManaRegenerate = character.connectedModel.manaregenerateBase + character.connectedModel.manaregenerateEffectsExtra;

                character.restoreHP(maxHealthRegenerate);
                character.restoreMP(maxManaRegenerate);

                character.connectedClient.writeMessage(new GameCharacterIncreaseVitality(character.healthCurrent, character.manaCurrent).getData());
            }

        }
    }
}
