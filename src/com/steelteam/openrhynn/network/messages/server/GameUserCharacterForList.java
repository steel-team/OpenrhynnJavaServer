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
package com.steelteam.openrhynn.network.messages.server;

import com.steelteam.openrhynn.models.CharacterModel;
import com.steelteam.openrhynn.network.messages.ORMessageIDs;
import com.steelteam.openrhynn.network.messages.ORMessage;

public class GameUserCharacterForList {

    private ORMessage _message = null;

    /* custom start */
    private int _objectId = 0;//4
    private int _classId = 0;
    private int _clanId = 0;//3
    private int _playfieldId = 0;//4
    private int _graphicsId = 0;
    private int _graphicsX = 0;
    private int _graphicsY = 0;
    private int _graphicsDim = 0;
    private int _x = 0;//2
    private int _y = 0;//2
    private int _level = 0;//1
    private int _levelPoints = 0;//2
    private int _experience = 0;//4
    private int _gold = 0;//4
    private int _healthBase = 0;
    private int _healthEffectsExtra = 0;//2
    private int _healthCurrent = 0;//2
    private int _manaBase = 0;
    private int _manaEffectsExtra = 0;//2
    private int _manaCurrent = 0;//2
    private int _attackBase = 0;
    private int _attackEffectsExtra = 0;
    private int _defenseBase = 0;
    private int _defenseEffectsExtra = 0;
    private int _damageBase = 0;
    private int _damageEffectsExtra = 0;
    private int _skillBase = 0;
    private int _skillEffectsExtra = 0;
    private int _magicBase = 0;
    private int _magicEffectsExtra = 0;
    private int _healthregenerateBase = 0;
    private int _healthregenerateEffectsExtra = 0;
    private int _manaregenerateBase = 0;
    private int _manaregenerateEffectsExtra = 0;
    private String _displayName = null;
    private String _ownStatus = null;


    private CharacterModel _character = null;

    /* custom end */

    public GameUserCharacterForList(CharacterModel character) {
        _message = new ORMessage(getMessageId());
        /* custom start */

        _character = character;

        _objectId = _character.id;
        _classId = _character.classId;
        _clanId = _character.clanId;
        _playfieldId = _character.worldId;
        _graphicsId = _character.graphicsId;
        _graphicsX = _character.graphicsX;
        _graphicsY = _character.graphicsY;
        _graphicsDim = _character.graphicsDim;

        _x = _character.x;
        _y = _character.y;
        _level = _character.level;
        _levelPoints = _character.levelPoints;
        _experience = _character.experience;
        _gold = _character.gold;

        _healthBase = _character.healthBase;
        _healthEffectsExtra = _character.healthEffectsExtra;
        _healthCurrent = _character.healthCurrent;
        _manaBase = _character.manaBase;
        _manaEffectsExtra = _character.manaEffectsExtra;
        _manaCurrent = _character.manaCurrent;
        _attackBase = _character.attackBase;
        _attackEffectsExtra = _character.attackEffectsExtra;
        _defenseBase = _character.defenseBase;
        _defenseEffectsExtra = _character.defenseEffectsExtra;
        _damageBase = _character.damageBase;
        _damageEffectsExtra = _character.damageEffectsExtra;
        _skillBase = _character.skillBase;
        _skillEffectsExtra = _character.skillEffectsExtra;
        _magicBase = _character.magicBase;
        _magicEffectsExtra = _character.magicEffectsExtra;
        _healthregenerateBase = _character.healthregenerateBase;
        _healthregenerateEffectsExtra = _character.healthregenerateEffectsExtra;
        _manaregenerateBase = _character.manaregenerateBase;
        _manaregenerateEffectsExtra = _character.manaregenerateEffectsExtra;
        _displayName = _character.displayName;
        _ownStatus = _character.ownStatus;



        _message.writeInt4(_objectId);
        _message.writeInt4(_classId);
        _message.writeInt3(_clanId);
        _message.writeInt4(_playfieldId);
        _message.writeInt4(_graphicsId);
        _message.writeInt2(_graphicsX);
        _message.writeInt2(_graphicsY);
        _message.writeByte((byte)_graphicsDim);

        _message.writeInt2(_x);
        _message.writeInt2(_y);
        _message.writeByte((byte)_level);
        _message.writeInt2(_levelPoints);
        _message.writeInt4(_experience);
        _message.writeInt4(_gold);

        _message.writeInt2(_healthBase);
        _message.writeInt2(_healthEffectsExtra);
        _message.writeInt2(_healthCurrent);

        _message.writeInt2(_manaBase);
        _message.writeInt2(_manaEffectsExtra);
        _message.writeInt2(_manaCurrent);

        _message.writeInt2(_attackBase);
        _message.writeInt2(_attackEffectsExtra);
        _message.writeInt2(_defenseBase);
        _message.writeInt2(_defenseEffectsExtra);
        _message.writeInt2(_damageBase);
        _message.writeInt2(_damageEffectsExtra);
        _message.writeInt2(_skillBase);
        _message.writeInt2(_skillEffectsExtra);
        _message.writeInt2(_magicBase);
        _message.writeInt2(_magicEffectsExtra);
        _message.writeInt2(_healthregenerateBase);
        _message.writeInt2(_healthregenerateEffectsExtra);
        _message.writeInt2(_manaregenerateBase);
        _message.writeInt2(_manaregenerateEffectsExtra);

        _message.writeString(_displayName);
        _message.writeString(_ownStatus);
        /* custom end */
    }

    public GameUserCharacterForList(int objectId, int classId, int clanId, int playfieldId, int graphicsId, int graphicsX, int graphicsY, int graphicsDim,
                                         int x, int y, int level, int levelPoints, int experience, int gold,
                                         int healthBase, int healthEffectsExtra, int healthCurrent, int manaBase, int manaEffectsExtra, int manaCurrent,
                                         int attackBase, int attackEffectsExtra,
                                         int defenseBase, int defenseEffectsExtra, int damageBase, int damageEffectsExtra, int skillBase, int skillEffectsExtra,
                                         int magicBase, int magicEffectsExtra, int healthregenerateBase, int healthregenerateEffectsExtra,
                                         int manaregenerateBase, int manaregenerateEffectsExtra, String displayName, String ownStatus) {
        _message = new ORMessage(getMessageId());
        /* custom start */
        _objectId = objectId;
        _classId = classId;
        _clanId = clanId;
        _playfieldId = playfieldId;
        _graphicsId = graphicsId;
        _graphicsX = graphicsX;
        _graphicsY = graphicsY;
        _graphicsDim = graphicsDim;

        _x = x;
        _y = y;
        _level = level;
        _levelPoints = levelPoints;
        _experience = experience;
        _gold = gold;

        _healthBase = healthBase;
        _healthEffectsExtra = healthEffectsExtra;
        _healthCurrent = healthCurrent;
        _manaBase = manaBase;
        _manaEffectsExtra = manaEffectsExtra;
        _manaCurrent = manaCurrent;
        _attackBase = attackBase;
        _attackEffectsExtra = attackEffectsExtra;
        _defenseBase = defenseBase;
        _defenseEffectsExtra = defenseEffectsExtra;
        _damageBase = damageBase;
        _damageEffectsExtra = damageEffectsExtra;
        _skillBase = skillBase;
        _skillEffectsExtra = skillEffectsExtra;
        _magicBase = magicBase;
        _magicEffectsExtra = magicEffectsExtra;
        _healthregenerateBase = healthregenerateBase;
        _healthregenerateEffectsExtra = healthregenerateEffectsExtra;
        _manaregenerateBase = manaregenerateBase;
        _manaregenerateEffectsExtra = manaregenerateEffectsExtra;
        _displayName = displayName;
        _ownStatus = ownStatus;



        _message.writeInt4(_objectId);
        _message.writeInt4(_classId);
        _message.writeInt3(_clanId);
        _message.writeInt4(_playfieldId);
        _message.writeInt4(_graphicsId);
        _message.writeInt2(_graphicsX);
        _message.writeInt2(_graphicsY);
        _message.writeByte((byte)_graphicsDim);

        _message.writeInt2(_x);
        _message.writeInt2(_y);
        _message.writeByte((byte)_level);
        _message.writeInt2(_levelPoints);
        _message.writeInt4(_experience);
        _message.writeInt4(_gold);

        _message.writeInt2(_healthBase);
        _message.writeInt2(_healthEffectsExtra);
        _message.writeInt2(_healthCurrent);

        _message.writeInt2(_manaBase);
        _message.writeInt2(_manaEffectsExtra);
        _message.writeInt2(_manaCurrent);

        _message.writeInt2(_attackBase);
        _message.writeInt2(_attackEffectsExtra);
        _message.writeInt2(_defenseBase);
        _message.writeInt2(_defenseEffectsExtra);
        _message.writeInt2(_damageBase);
        _message.writeInt2(_damageEffectsExtra);
        _message.writeInt2(_skillBase);
        _message.writeInt2(_skillEffectsExtra);
        _message.writeInt2(_magicBase);
        _message.writeInt2(_magicEffectsExtra);
        _message.writeInt2(_healthregenerateBase);
        _message.writeInt2(_healthregenerateEffectsExtra);
        _message.writeInt2(_manaregenerateBase);
        _message.writeInt2(_manaregenerateEffectsExtra);

        _message.writeString(_displayName);
        _message.writeString(_ownStatus);
        /* custom end */
    }

    public GameUserCharacterForList(ORMessage message) {
        _message = message;
        /* custom start */
        _objectId = message.readIntFrom4();
        _classId = message.readIntFrom4();
        _clanId = message.readIntFrom3();
        _playfieldId = message.readIntFrom4();
        _graphicsId = message.readIntFrom4();
        _graphicsX = message.readIntFrom2();
        _graphicsY = message.readIntFrom2();
        _graphicsDim = message.readByte();

        _x = message.readIntFrom2();
        _y = message.readIntFrom2();
        _level = message.readByte();
        _levelPoints = message.readIntFrom2();
        _experience = message.readIntFrom4();
        _gold = message.readIntFrom4();

        _healthBase = message.readIntFrom2();
        _healthEffectsExtra = message.readIntFrom2();
        _healthCurrent = message.readIntFrom2();

        _manaBase = message.readIntFrom2();
        _manaEffectsExtra = message.readIntFrom2();
        _manaCurrent = message.readIntFrom2();

        _attackBase = message.readIntFrom2();
        _attackEffectsExtra = message.readIntFrom2();
        _defenseBase = message.readIntFrom2();
        _defenseEffectsExtra = message.readIntFrom2();
        _damageBase = message.readIntFrom2();
        _damageEffectsExtra = message.readIntFrom2();
        _skillBase = message.readIntFrom2();
        _skillEffectsExtra = message.readIntFrom2();
        _magicBase = message.readIntFrom2();
        _magicEffectsExtra = message.readIntFrom2();
        _healthregenerateBase = message.readIntFrom2();
        _healthregenerateEffectsExtra = message.readIntFrom2();
        _manaregenerateBase = message.readIntFrom2();
        _manaregenerateEffectsExtra = message.readIntFrom2();
        _displayName = message.readString();
        _ownStatus = message.readString();

        _character = new CharacterModel();
        _character.attackBase = _attackBase;
        _character.attackEffectsExtra = _attackEffectsExtra;
        _character.clanId = _clanId;
        _character.classId = _classId;
        _character.damageBase = _damageBase;
        _character.damageEffectsExtra = _damageEffectsExtra;
        _character.defenseBase = _defenseBase;
        _character.defenseEffectsExtra = _defenseEffectsExtra;
        _character.displayName = _displayName;
        _character.experience = _experience;
        _character.gold = _gold;
        _character.graphicsDim = _graphicsDim;
        _character.graphicsId = _graphicsId;
        _character.graphicsX = _graphicsX;
        _character.graphicsY = _graphicsY;
        _character.healthBase = _healthBase;
        _character.healthCurrent = _healthCurrent;
        _character.healthEffectsExtra = _healthEffectsExtra;
        _character.healthregenerateBase = _healthregenerateBase;
        _character.healthregenerateEffectsExtra = _healthregenerateEffectsExtra;
        _character.id = _objectId;
        _character.level = _level;
        _character.levelPoints = _levelPoints;
        _character.magicBase = _magicBase;
        _character.magicEffectsExtra = _magicEffectsExtra;
        _character.manaBase = _manaBase;
        _character.manaCurrent = _manaCurrent;
        _character.manaEffectsExtra = _manaEffectsExtra;
        _character.manaregenerateBase = _manaregenerateBase;
        _character.manaregenerateEffectsExtra = _manaregenerateEffectsExtra;
        _character.ownStatus = _ownStatus;
        _character.skillBase = _skillBase;
        _character.skillEffectsExtra = _skillEffectsExtra;
        _character.worldId = _playfieldId;
        _character.x = _x;
        _character.y = _y;

        /* custom end */
    }

    public int getMessageId() {
        return ORMessageIDs.MSGID_GAME_USER_CHARACTER_FOR_LIST;
    }

    public ORMessage getData() {
        return _message;
    }

    /* message custom */

    public int getObjectId() {
        return _objectId;
    }

    public int getClassId() {
        return _classId;
    }

    public int getClanId() {
        return _clanId;
    }

    public int getPlayfieldId() {
        return _playfieldId;
    }

    public int getGraphicsId() {
        return _graphicsId;
    }

    public int getGraphicsX() {
        return _graphicsX;
    }

    public int getGraphicsY() {
        return _graphicsY;
    }

    public int getGraphicsDim() {
        return _graphicsDim;
    }


    public int getX() {
        return _x;
    }
    public int getY() {
        return _y;
    }

    public int getLevel() {
        return _level;
    }

    public int getLevelPoints() {
        return _levelPoints;
    }

    public int getExperience() {
        return _experience;
    }
    public int getGold() {
        return _gold;
    }


    public int getHealthBase() {
        return _healthBase;
    }

    public int getHealthEffectsExtra() {
        return _healthEffectsExtra;
    }
    public int getHealthCurrent() {
        return _healthCurrent;
    }

    public int getManaBase() {
        return _manaBase;
    }

    public int getManaEffectsExtra() {
        return _manaEffectsExtra;
    }
    public int get_manaCurrent() {
        return _manaCurrent;
    }

    public int getAttackBase() {
        return _attackBase;
    }

    public int getAttackEffectsExtra() {
        return _attackEffectsExtra;
    }

    public int getDefenseBase() {
        return _defenseBase;
    }

    public int getDefenseEffectsExtra() {
        return _defenseEffectsExtra;
    }

    public int getDamageBase() {
        return _damageBase;
    }

    public int getDamageEffectsExtra() {
        return -_damageEffectsExtra;
    }

    public int getSkillBase() {
        return _skillBase;
    }

    public int getSkillEffectsExtra() {
        return _skillEffectsExtra;
    }

    public int getMagicBase() {
        return _magicBase;
    }

    public int getMagicEffectsExtra() {
        return _magicEffectsExtra;
    }

    public int getHealthRegenerateBase() {
        return _healthregenerateBase;
    }

    public int getHealthRegenerateEffectsExtra() {
        return _healthregenerateEffectsExtra;
    }

    public int getManaRegenerateBase() {
        return _manaregenerateBase;
    }

    public int getManaRegenerateEffectsExtra() {
        return _manaregenerateEffectsExtra;
    }

    public String getDisplayName() {
        return _displayName;
    }

    public String getOwnStatus() {
        return _ownStatus;
    }

    public CharacterModel getCharacter() {
        return _character;
    }

    /* message custom end */
}
