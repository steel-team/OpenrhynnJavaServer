/**
 *
 * @author marlowe
 * LICENSE: https://github.com/marlowe-fw/Rhynn/blob/master/LICENSE
 */
package com.steelteam.openrhynn.network.messages;

public class ORMessageIDs {

    // %%GENERATOR_START%%MSG_IDS%%
	public static final int MSGID_GAME_CHARACTER_MOVE = 2700;
	public static final int MSGID_GAME_CHARACTER_MOVE_INFO = 2705;
	public static final int MSGID_GAME_CHARACTER_ADD = 2710;
	public static final int MSGID_GAME_CHARACTER_REMOVE = 2713;
	public static final int MSGID_GAME_CHARACTER_CHAT_ALL_REQUEST = 2740;
	public static final int MSGID_GAME_CHARACTER_CHAT_ALL_INFO = 2741;
	public static final int MSGID_GAME_CHARACTER_CHAT_REQUEST = 2750;
	public static final int MSGID_GAME_CHARACTER_CHAT_INFO = 2751;
	public static final int MSGID_GAME_CHARACTER_ATTACK_REQUEST = 2800;
	public static final int MSGID_GAME_CHARACTER_HIT_INFO = 2803;
	public static final int MSGID_GAME_CHARACTER_HIT_MISS_INFO = 2804;
	public static final int MSGID_GAME_CHARACTER_KILLED = 2820;
	public static final int MSGID_GAME_USER_CHARACTER_CLASS_FOR_LIST = 2150;
	public static final int MSGID_GAME_USER_GET_CHARACTERS_REQUEST = 2160;
	public static final int MSGID_GAME_USER_CHARACTER_FOR_LIST = 2162;
	public static final int MSGID_GAME_USER_CHARACTER_CREATE_PERMISSION_REQUEST = 2180;
	public static final int MSGID_GAME_USER_CHARACTER_CREATE_PERMISSION_RESULT = 2181;
	public static final int MSGID_GAME_USER_CHARACTER_CREATE_REQUEST = 2182;
	public static final int MSGID_GAME_USER_CHARACTER_CREATE_RESULT = 2183;
	public static final int MSGID_GAME_USER_CHARACTER_DELETE_REQUEST = 2195;
	public static final int MSGID_GAME_USER_CHARACTER_DELETE_RESULT = 2196;
	public static final int MSGID_GAME_USER_CHARACTER_SELECT_REQUEST = 2200;
	public static final int MSGID_GAME_CHARACTER_HIGHSCORE_REQUEST = 2204;
	public static final int MSGID_GAME_CHARACTER_HIGHSCORE_LIST_ENTRY = 2205;
	public static final int MSGID_GAME_CHARACTER_RESPAWN_REQUEST = 2207;
	public static final int MSGID_GAME_CHARACTER_RESPAWN_RESULT = 2208;
	public static final int MSGID_GAME_CHARACTER_INCREASE_VITALITY = 2215;
	public static final int MSGID_GAME_FRIEND_LIST_REQUEST = 2350;
	public static final int MSGID_GAME_FRIEND_LIST_END = 2351;
	public static final int MSGID_GAME_PING = 1024;
	public static final int MSGID_GAME_PONG = 1025;
	public static final int MSGID_GAME_SERVER_LIST_REQUEST = 1028;
	public static final int MSGID_GAME_SERVER_ENTRY = 1029;
	public static final int MSGID_GAME_VERSION_REQUEST = 1030;
	public static final int MSGID_GAME_VERSION = 1031;
	public static final int MSGID_GAME_GRAPHICS_LOAD_REQUEST = 1060;
	public static final int MSGID_GAME_GRAPHICS_LOAD_INFO = 1061;
	public static final int MSGID_GAME_GRAPHICS_LOAD_CHUNK = 1062;
	public static final int MSGID_GAME_ITEM_ADD = 2410;
	public static final int MSGID_GAME_ITEM_REMOVE = 2412;
	public static final int MSGID_GAME_ITEM_PICKUP_REQUEST = 2424;
	public static final int MSGID_GAME_ITEM_INVENTORY_ADD = 2425;
	public static final int MSGID_GAME_ITEM_INVENTORY_ADD_FAIL = 2426;
	public static final int MSGID_GAME_ITEM_INVENTORY_END = 2427;
	public static final int MSGID_GAME_ITEM_EQUIP_REQUEST = 2430;
	public static final int MSGID_GAME_ITEM_UNEQUIP_REQUEST = 2432;
	public static final int MSGID_GAME_ITEM_DROP_REQUEST = 2440;
	public static final int MSGID_GAME_ITEM_USE_REQUEST = 2460;
	public static final int MSGID_GAME_PLAYFIELD_ENTER_WORLD_REQUEST = 2310;
	public static final int MSGID_GAME_PLAYFIELD_INFO = 2320;
	public static final int MSGID_GAME_PLAYFIELD_GRAPHICS_INFO = 2321;
	public static final int MSGID_GAME_PLAYFIELD_LOAD_REQUEST = 2325;
	public static final int MSGID_GAME_PLAYFIELD_LOAD_CHUNK = 2327;
	public static final int MSGID_GAME_PLAYFIELD_ENTER_REQUEST = 2330;
	public static final int MSGID_GAME_PLAYFIELD_ENTER_RESULT = 2332;
	public static final int MSGID_GAME_USER_REGISTER_REQUEST = 2050;
	public static final int MSGID_GAME_USER_REGISTER_RESULT = 2051;
	public static final int MSGID_GAME_USER_GET_EMAIL_REQUEST = 2055;
	public static final int MSGID_GAME_USER_GET_EMAIL_RESULT = 2056;
	public static final int MSGID_GAME_USER_EMAIL_CHANGE_REQUEST = 2057;
	public static final int MSGID_GAME_USER_EMAIL_CHANGE_RESULT = 2058;
	public static final int MSGID_GAME_USER_LOGIN_REQUEST = 2065;
	public static final int MSGID_GAME_USER_LOGIN_RESULT = 2066;
	public static final int MSGID_GAME_USER_FORCED_LOGOUT = 2070;
	public static final int MSGID_GAME_USER_CHALLENGENUMBER = 2075;
	public static final int MSGID_GAME_USER_PASSWORD_RESET_CODE_REQUEST = 2100;
	public static final int MSGID_GAME_USER_PASSWORD_RESET_CODE_RESULT = 2101;
	public static final int MSGID_GAME_USER_PASSWORD_RESET_NEW_REQUEST = 2104;
	public static final int MSGID_GAME_USER_PASSWORD_RESET_NEW_RESULT = 2105;
        
        //NEW MESSAGES        
        public static final int MSGID_GAME_CHARACTER_UPDATE_DATA = 2216;
        public static final int MSGID_GAME_CHARACTER_ATTRIBUTE_INCREASE = 2217;
        public static final int MSGID_GAME_FRIEND_ADD_REQUEST = 2219;
        public static final int MSGID_GAME_FRIEND_ADD_PLAYER = 2220;
        public static final int MSGID_GAME_FRIEND_ADD_RESPONSE = 2221;
        public static final int MSGID_GAME_FRIEND_ADDTOLIST = 2223;
        public static final int MSGID_GAME_FRIEND_DELETE = 2225;
        public static final int MSGID_GAME_FRIEND_STATUS = 2271;
        public static final int MSGID_GAME_INFO = 2224;
        public static final int MSGID_GAME_REQUEST_BUYLIST = 2226;
        public static final int MSGID_GAME_TRADER_ADD = 2227;
        public static final int MSGID_GAME_SET_SUBSTATENORMAL = 2228;
        public static final int MSGID_GAME_BUY_ITEM_REQUEST = 2229;
        public static final int MSGID_GAME_DIALOG_CLIENT = 2230;
        public static final int MSGID_GAME_DIALOG_BOT = 2231;
        public static final int MSGID_GAME_DIALOG_REQUEST = 2232;
        public static final int MSGID_GAME_DIALOG_FAIL = 2233;
        public static final int MSGID_GAME_DIALOG_FINAL = 2234;
        public static final int MSGID_GAME_UPDATE_CORDS = 2236;
        public static final int MSGID_GAME_OPEN_QUEST_REQUEST = 2237;
        public static final int MSGID_GAME_QUEST_DETAILS_REQUEST = 2238;
        public static final int MSGID_GAME_QUEST_LEAVE_REQUEST = 2239;
        
        public static final int MSGID_GAME_QUEST_ENTRY = 2240;
        public static final int MSGID_GAME_QUEST_DETAILS_RESULT = 2241;
        public static final int MSGID_GAME_QUEST_LEAVE_RESULT = 2242;
        public static final int MSGID_GAME_QUEST_FINISH = 2243;
        public static final int MSGID_GAME_QUEST_UPDATED = 2244;
        
        public static final int MSGID_GAME_HELP_REQUEST = 2246;
        public static final int MSGID_GAME_HELP_ENTRY = 2245;
        public static final int MSGID_GAME_DIALOG_SILENT = 2247;
        public static final int MSGID_GAME_CHARACTER_UPDATE_ALL_SILENT = 2248;
        public static final int MSGID_GAME_ITEM_SETUNITS = 2249;
        public static final int MSGID_GAME_INFOOVERLAY = 2250;
        public static final int MSGID_GAME_CANCELSALE = 2251;
        public static final int MSGID_GAME_SALEOFFER = 2252;
        public static final int MSGID_GAME_SCANCELSALE = 2251;
        public static final int MSGID_GAME_ITUSE = 2252;
        public static final int MSGID_GAME_TRIGGERGROUND = 2253;
        public static final int MSGID_GAME_FIREWALL = 2254;
        public static final int MSGID_GAME_FIREWALL_REMOVE = 2270;
        public static final int MSGID_GAME_TRIGGERTP = 2255;
        public static final int MSGID_GAME_TRIGGERUSE = 2256;
        public static final int MSGID_GAME_REMOVEINV = 2258;
        public static final int MSGID_GAME_UPDATEGOLD = 2259;
        public static final int MSGID_GAME_BLOCK = 2260;
        public static final int MSGID_GAME_SPEED = 2261;
        public static final int MSGID_GAME_ADDBELT = 2262;
        public static final int MSGID_GAME_BELTREMOVE = 2263;
        public static final int MSGID_GAME_BELT_ENTRY = 2264;
        
        public static final int MSGID_GAME_SPELL_VISUAL = 2265;
        public static final int MSGID_GAME_MORPH = 2266;
        public static final int MSGID_GAME_CHARACTER_UPDATE_ALL2=2267;
        public static final int MSGID_GAME_UNLOCK = 2268;
        public static final int MSGID_GAME_TARGET = 2269;
        public static final int MSGID_GAME_USE_PORTAL = 2235;

        //MUST BE REMOVED
        //public static final int MSGID_GAME_TELEPORT = 2257;
        //public static final int MSGID_GAME_CHARACTER_UPDATE_ALL = 2218;
        //public static final int MSGID_GAME_TPREQUEST = 2255;
        //public static final int MSGID_GAME_TPACCEPT = 2256;
		//public static final int MSGID_GAME_USER_CHARACTER_RENAME_REQUEST = 2190;
		//public static final int MSGID_GAME_USER_CHARACTER_RENAME_RESULT = 2191;
        //public static final int MSGID_GAME_FRIEND_ADD_RESULT = 2222;
        
    // %%GENERATOR_END%%MSG_IDS%%
}