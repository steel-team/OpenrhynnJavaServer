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

import com.steelteam.openrhynn.models.CharacterClassModel;
import com.steelteam.openrhynn.network.messages.ORMessageIDs;
import com.steelteam.openrhynn.network.messages.ORMessage;

public class GameUserCharacterClassForList {

    private ORMessage _message = null;

    /* custom start */
    private int _classId = 0;
    private boolean _premiumOnly = false;
    private int _graphicsId = 0;
    private int _graphicsX = 0;
    private int _graphicsY = 0;
    private int _graphicsDim = 0;
    private int _healthBase = 0;
    private int _healthModifier = 0;
    private int _manaBase = 0;
    private int _manaModifier = 0;
    private int _attackBase = 0;
    private int _attackModifier = 0;
    private int _defenseBase = 0;
    private int _defenseModifier = 0;
    private int _damageBase = 0;
    private int _damageModifier = 0;
    private int _skillBase = 0;
    private int _skillModifier = 0;
    private int _magicBase = 0;
    private int _magicModifier = 0;
    private int _healthregenerateBase = 0;
    private int _healthregenerateModifier = 0;
    private int _manaregenerateBase = 0;
    private int _manaregenerateModifier = 0;
    private String _displayName = null;
    private CharacterClassModel _character = null;

    /* custom end */


    public GameUserCharacterClassForList(CharacterClassModel character) {
        _message = new ORMessage(getMessageId());
        /* custom start */
        _character = character;
        _classId = _character.classId;
        _premiumOnly = _character.premiumOnly;
        _graphicsId = _character.graphicsId;
        _graphicsX = _character.graphicsX;
        _graphicsY = _character.graphicsY;
        _graphicsDim = _character.graphicsDim;

        _healthBase = _character.healthBase;
        _healthModifier = _character.healthModifier;
        _manaBase = _character.manaBase;
        _manaModifier = _character.manaModifier;
        _attackBase = _character.attackBase;
        _attackModifier = _character.attackModifier;
        _defenseBase = _character.defenseBase;
        _defenseModifier = _character.defenseModifier;
        _damageBase = _character.damageBase;
        _damageModifier = _character.damageModifier;
        _skillBase = _character.skillBase;
        _skillModifier = _character.skillModifier;
        _magicBase = _character.magicBase;
        _magicModifier = _character.magicModifier;
        _healthregenerateBase = _character.healthregenerateBase;
        _healthregenerateModifier = _character.healthregenerateModifier;
        _manaregenerateBase = _character.manaregenerateBase;
        _manaregenerateModifier = _character.manaregenerateModifier;
        _displayName = _character.displayName;



        _message.writeInt4(_classId);
        _message.writeByte(_premiumOnly ? (byte)1 : (byte)0);
        _message.writeInt4(_graphicsId);
        _message.writeInt2(_graphicsX);
        _message.writeInt2(_graphicsY);
        _message.writeByte((byte)_character.graphicsDim);

        _message.writeInt2(_healthBase);
        _message.writeByte((byte)_healthModifier);
        _message.writeInt2(_manaBase);
        _message.writeByte((byte)_manaModifier);
        _message.writeInt2(_attackBase);
        _message.writeByte((byte)_attackModifier);
        _message.writeInt2(_defenseBase);
        _message.writeByte((byte)_defenseModifier);
        _message.writeInt2(_damageBase);
        _message.writeByte((byte)_damageModifier);
        _message.writeInt2(_skillBase);
        _message.writeByte((byte)_skillModifier);
        _message.writeInt2(_magicBase);
        _message.writeByte((byte)_magicModifier);
        _message.writeInt2(_healthregenerateBase);
        _message.writeByte((byte)_healthregenerateModifier);
        _message.writeInt2(_manaregenerateBase);
        _message.writeByte((byte)_manaregenerateModifier);
        _message.writeString(_displayName);
        /* custom end */
    }

    public GameUserCharacterClassForList(int classId, boolean premiumOnly, int graphicsId, int graphicsX, int graphicsY, int graphicsDim,
                                         int healthBase, int healthModifier, int manaBase, int manaModifier, int attackBase, int attackModifier,
                                         int defenseBase, int defenseModifier, int damageBase, int damageModifier, int skillBase, int skillModifier,
                                         int magicBase, int magicModifier, int healthregenerateBase, int healthregenerateModifier,
                                         int manaregenerateBase, int manaregenerateModifier, String displayName) {
        _message = new ORMessage(getMessageId());
        /* custom start */
        _classId = classId;
        _premiumOnly = premiumOnly;
        _graphicsId = graphicsId;
        _graphicsX = graphicsX;
        _graphicsY = graphicsY;
        _graphicsDim = graphicsDim;

        _healthBase = healthBase;
        _healthModifier = healthModifier;
        _manaBase = manaBase;
        _manaModifier = manaModifier;
        _attackBase = attackBase;
        _attackModifier = attackModifier;
        _defenseBase = defenseBase;
        _defenseModifier = defenseModifier;
        _damageBase = damageBase;
        _damageModifier = damageModifier;
        _skillBase = skillBase;
        _skillModifier = skillModifier;
        _magicBase = magicBase;
        _magicModifier = magicModifier;
        _healthregenerateBase = healthregenerateBase;
        _healthregenerateModifier = healthregenerateModifier;
        _manaregenerateBase = manaregenerateBase;
        _manaregenerateModifier = manaregenerateModifier;
        _displayName = displayName;



        _message.writeInt4(_classId);
        _message.writeByte(_premiumOnly ? (byte)1 : (byte)0);
        _message.writeInt4(_graphicsId);
        _message.writeInt2(_graphicsX);
        _message.writeInt2(_graphicsY);
        _message.writeByte((byte)_graphicsDim);

        _message.writeInt2(_healthBase);
        _message.writeByte((byte)_healthModifier);
        _message.writeInt2(_manaBase);
        _message.writeByte((byte)_manaModifier);
        _message.writeInt2(_attackBase);
        _message.writeByte((byte)_attackModifier);
        _message.writeInt2(_defenseBase);
        _message.writeByte((byte)_defenseModifier);
        _message.writeInt2(_damageBase);
        _message.writeByte((byte)_damageModifier);
        _message.writeInt2(_skillBase);
        _message.writeByte((byte)_skillModifier);
        _message.writeInt2(_magicBase);
        _message.writeByte((byte)_magicModifier);
        _message.writeInt2(_healthregenerateBase);
        _message.writeByte((byte)_healthregenerateModifier);
        _message.writeInt2(_manaregenerateBase);
        _message.writeByte((byte)_manaregenerateModifier);
        _message.writeString(_displayName);
        /* custom end */
    }

    public GameUserCharacterClassForList(ORMessage message) {
        _message = message;
        /* custom start */
        _character = new CharacterClassModel();

        _classId = message.readIntFrom4();
        _premiumOnly = (message.readByte() != 0);
        _graphicsId = message.readIntFrom4();
        _graphicsX = message.readIntFrom2();
        _graphicsY = message.readIntFrom2();
        _graphicsDim = message.readByte();

        _healthBase = message.readIntFrom2();
        _healthModifier = message.readByte();
        _manaBase = message.readIntFrom2();
        _manaModifier = message.readByte();
        _attackBase = message.readIntFrom2();
        _attackModifier = message.readByte();
        _defenseBase = message.readIntFrom2();
        _defenseModifier = message.readByte();
        _damageBase = message.readIntFrom2();
        _damageModifier = message.readByte();
        _skillBase = message.readIntFrom2();
        _skillModifier = message.readByte();
        _magicBase = message.readIntFrom2();
        _magicModifier = message.readByte();
        _healthregenerateBase = message.readIntFrom2();
        _healthregenerateModifier = message.readByte();
        _manaregenerateBase = message.readIntFrom2();
        _manaregenerateModifier = message.readByte();
        _displayName = message.readString();

        _character.classId = _classId;
        _character.premiumOnly = _premiumOnly;
        _character.graphicsId = _graphicsId;
        _character.graphicsX = _graphicsX;
        _character.graphicsY = _graphicsY;
        _character.graphicsDim = _graphicsDim;
        _character.healthBase = _healthBase;
        _character.healthModifier = _healthModifier;
        _character.manaBase = _manaBase;
        _character.manaModifier = _manaModifier;
        _character.attackBase = _attackBase;
        _character.attackModifier = _attackModifier;
        _character.defenseBase = _defenseBase;
        _character.defenseModifier = _defenseModifier;
        _character.damageBase = _damageBase;
        _character.damageModifier = _damageModifier;
        _character.skillBase = _skillBase;
        _character.skillModifier = _skillModifier;
        _character.magicBase = _magicBase;
        _character.magicModifier = _magicModifier;
        _character.healthregenerateBase = _healthregenerateBase;
        _character.healthregenerateModifier = _healthregenerateModifier;
        _character.manaregenerateBase = _manaregenerateBase;
        _character.manaregenerateModifier = _manaregenerateModifier;
        _character.displayName = _displayName;
        /* custom end */
    }

    public int getMessageId() {
        return ORMessageIDs.MSGID_GAME_USER_CHARACTER_CLASS_FOR_LIST;
    }

    public ORMessage getData() {
        return _message;
    }

    /* message custom */

    public int getClassId() {
        return _classId;
    }
    public boolean getPremiumOnly(){
        return _premiumOnly;
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

    public int getHealthBase() {
        return _healthBase;
    }

    public int getHealthModifier() {
        return _healthModifier;
    }

    public int getManaBase() {
        return _manaBase;
    }

    public int getManaModifier() {
        return _manaModifier;
    }

    public int getAttackBase() {
        return _attackBase;
    }

    public int getAttackModifier() {
        return _attackModifier;
    }

    public int getDefenseBase() {
        return _defenseBase;
    }

    public int getDefenseModifier() {
        return _defenseModifier;
    }

    public int getDamageBase() {
        return _damageBase;
    }

    public int getDamagerModifier() {
        return _damageModifier;
    }

    public int getSkillBase() {
        return _skillBase;
    }

    public int getSkillModifier() {
        return _skillModifier;
    }

    public int getMagicBase() {
        return _magicBase;
    }

    public int getMagicModifier() {
        return _magicModifier;
    }

    public int getHealthRegenerateBase() {
        return _healthregenerateBase;
    }

    public int getHealthRegenerateModifier() {
        return _healthregenerateModifier;
    }

    public int getManaRegenerateBase() {
        return _manaregenerateBase;
    }

    public int getManaRegenerateModifier() {
        return _manaregenerateModifier;
    }

    public String getDisplayName() {
        return _displayName;
    }

    public CharacterClassModel GetCharacterClassModel() {
        return _character;
    }

    /* message custom end */
}
