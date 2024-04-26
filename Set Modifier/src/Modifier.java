import java.awt.Color;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import javax.swing.SwingWorker;



public class Modifier
{
	
	
	
	public static String loadPath;
	
	public static String backupPath;
	
	public static List<String> headers = new ArrayList<String>();
	
	public static List<List<String>> cards = new ArrayList<List<String>>();
	
	
	
	public static Map<String, String> colorWordsMap = new LinkedHashMap<String, String>() 
	{
		private static final long serialVersionUID = 1L;
	{
		put("black", "rgb(0,0,0)");
		put("white", "rgb(255,255,255)");
		put("grey", "rgb(200,200,200)");
		put("gold", "rgb(200,180,0)");
	}};
	
	
	
	public static List<String> typeList = new ArrayList<String>(Arrays.asList(
		"Artifact",
		"Battle",
		"Creature",
		"Kindred",
		"Tribal",
		"Enchantment",
		"Land",
		"Planeswalker",
		"Instant",
		"Sorcery",
		"Conspiracy",
		"Dungeon",
		"Emblem",
		"Hero",
		"Token",
		"Phenomenon",
		"Plane",
		"Scheme",
		"Vanguard"
	));
	
	
	
	public static List<String> superTypeList = new ArrayList<String>(Arrays.asList(
		"Basic",
		"Elite",
		"Host",
		"Legendary",
		"Ongoing",
		"Snow",
		"World"
	));
	
	
	
	public static Map<String, String> keyMap = new LinkedHashMap<String, String>() 
	{
		private static final long serialVersionUID = 1L;
	{
		put("NAME",			"	name:");
		put("CASTING COST",	"	casting_cost:");
		put("SUPER TYPE",	"	super_type:");
		put("TYPE",			"	super_type:");
		put("SUB TYPE",		"	sub_type:");
		put("POWER",		"	power:");
		put("TOUGHNESS",	"	toughness:");
		put("LOYALTY",		"	loyalty:");
		put("RARITY",		"	rarity:");
		put("RULE TEXT",	"	rule_text:");
		put("FLAVOR TEXT",	"	flavor_text:");
		put("WATERMARK",	"	watermark:");
		put("STAMP",		"	card_stamp:");
		put("ARTIST",		"	illustrator:");
		put("TEMPLATE",		"	stylesheet:");
		put("BORDER",		"	border_color:");
		put("NOTES",		"	notes:");
	}};
	
	
	
	public static Map<String, String> defaultMap = new LinkedHashMap<String, String>() 
	{
		private static final long serialVersionUID = 1L;
	{
		put("	name:",			"");
		put("	casting_cost:",	"");
		put("	super_type:",	"");
		put("	super_type:",	"");
		put("	sub_type:",		"");
		put("	power:",		"");
		put("	toughness:",	"");
		put("	loyalty:",		"");
		put("	rarity:",		"common");
		put("	rule_text:",	"");
		put("	flavor_text:",	"");
		put("	watermark:",	"none");
		put("	card_stamp:",	"standard");
		put("	illustrator:",	"");
		put("	stylesheet:",	"");
		put("	border_color:",	"rgb(0,0,0)");
		put("	notes:",		"");
	}};
	
	
	
	public static BiPredicate<String, String> conditionOperationEquals = (a, b) -> a.trim().equals(b.trim()) || a.trim().equals(removeTags(b).trim());
	
	public static BiPredicate<String, String> conditionOperationNotEquals = (a, b) -> !conditionOperationEquals.test(a, b);
	
	public static BiPredicate<String, String> conditionOperationContains = (a, b) -> b.contains(a.trim()) || removeTags(b).contains(a.trim());
	
	public static BiPredicate<String, String> conditionOperationNotContains = (a, b) -> !conditionOperationContains.test(a, b);
	
	public static BiPredicate<String, String> conditionOperationNumberGreater = (a, b) -> b.equals("") ? false : parseInt(a).intValue() < parseInt(b).intValue();
	
	public static BiPredicate<String, String> conditionOperationNumberSmaller = (a, b) -> b.equals("") ? false : parseInt(a).intValue() > parseInt(b).intValue();
	
	public static BiPredicate<String, String> conditionOperationColorEquals = (a, b) ->
	{
		
		String[] rgbAsked = a.trim().replace("rgb", "").replace("(", "").replace(")", "").split(" *[.,:;] *");
		
		String[] rgbFound = a.trim().replace("rgb", "").replace("(", "").replace(")", "").split(" *[.,:;] *");
		
		return conditionOperationEquals.test(rgbAsked[0], rgbFound[0]) && conditionOperationEquals.test(rgbAsked[1], rgbFound[1]) && conditionOperationEquals.test(rgbAsked[2], rgbFound[2]);
		
	};
	
	public static BiPredicate<String, String> conditionOperationColorNotEquals = (a, b) -> !conditionOperationColorEquals.test(a, b);
	
	public static BiPredicate<String, String> conditionOperationColorWordEquals = (a, b) -> conditionOperationColorEquals.test(colorWordsMap.get(a.replaceAll("^is ", "")), b);
	
	public static BiPredicate<String, String> conditionOperationTypeEquals = (a, b) ->
	{
		
		for (int i = 0; i < superTypeList.size(); i++)
		{
			
			b = b.replace(superTypeList.get(i), "");
			
		}
		
		b = removeTags(b).trim().replace("  ", " ");
		
		return a.trim().equals(b);
		
	};
	
	public static BiPredicate<String, String> conditionOperationTypeNotEquals = (a, b) -> !conditionOperationTypeEquals.test(a, b);
	
	public static BiPredicate<String, String> conditionOperationSuperTypeEquals = (a, b) ->
	{
		
		for (int i = 0; i < typeList.size(); i++)
		{
			
			b = b.replace(typeList.get(i), "");
			
		}
		
		b = removeTags(b).trim().replace("  ", " ");
		
		return a.trim().equals(b);
		
	};
	
	public static BiPredicate<String, String> conditionOperationSuperTypeNotEquals = (a, b) -> !conditionOperationSuperTypeEquals.test(a, b);
	
	public static BiPredicate<String, String> conditionOperationTemplateEquals = (a, b) -> a.replaceAll("^magic-", "").replaceAll("\\.mse-style$", "").toLowerCase().equals(b);
	
	public static BiPredicate<String, String> conditionOperationOtherEquals = (a, b) -> conditionOperationEquals.test(a.replaceAll("^is ", ""), b);
	
	public static List<String> defaultTextConditionOptions = new ArrayList<String>(Arrays.asList(
		"is the following :",
		"is not the following :",
		"contains the following :",
		"does not contain the following :"
	));
	
	public static List<String> defaultNumberConditionOptions = new ArrayList<String>(Arrays.asList(
		"is the following :",
		"is not the following :",
		"is greater than the following number :",
		"is smaller than the following number :"
	));
	
	public static Map<String, List<String>> conditionOptions = new LinkedHashMap<String, List<String>>() 
	{
		private static final long serialVersionUID = 1L;
	{
		put("If the card's NAME", defaultTextConditionOptions);
		put("If the card's CASTING COST", defaultTextConditionOptions);
		put("If the card's SUPER TYPE", new ArrayList<String>(Arrays.asList(
			"is the following super type :",
			"is not the following super type :",
			"contains the following :",
			"does not contain the following :"
		)));
		put("If the card's TYPE", new ArrayList<String>(Arrays.asList(
			"is the following type :",
			"is not the following type :",
			"contains the following :",
			"does not contain the following :"
		)));
		put("If the card's SUB TYPE", defaultTextConditionOptions);
		put("If the card's POWER", defaultNumberConditionOptions);
		put("If the card's TOUGHNESS", defaultNumberConditionOptions);
		put("If the card's LOYALTY", defaultNumberConditionOptions);
		put("If the card's RARITY", new ArrayList<String>(Arrays.asList(
			"is basic land",
			"is common",
			"is uncommon",
			"is rare",
			"is mythic rare",
			"is masterpiece",
			"is special"
		)));
		put("If the card's RULE TEXT", defaultTextConditionOptions);
		put("If the card's FLAVOR TEXT", defaultTextConditionOptions);
		put("If the card's WATERMARK", new ArrayList<String>(Arrays.asList(
			"is none",
			"is mana symbol white",
			"is mana symbol blue",
			"is mana symbol black",
			"is mana symbol red",
			"is mana symbol green",
			"is mana symbol colorless",
			"is mana symbol snow",
			"is transparent mana symbol white",
			"is transparent mana symbol blue",
			"is transparent mana symbol black",
			"is transparent mana symbol red",
			"is transparent mana symbol green",
			"is guild symbol The Azorius Senate (W/U)",
			"is guild symbol House Dimir (U/B)",
			"is guild symbol The Cult of Rakdos (B/R)",
			"is guild symbol The Gruul Clans (R/G)",
			"is guild symbol The Selesnya Conclave (G/W)",
			"is guild symbol The Orzhov Syndicate (W/B)",
			"is guild symbol The Izzet (U/R)",
			"is guild symbol The Golgari (B/G)",
			"is guild symbol The Boros Legion (R/W)",
			"is guild symbol The Simic Combine (G/U)",
			"is guild symbol originals The Azorius Senate (W/U)",
			"is guild symbol originals House Dimir (U/B)",
			"is guild symbol originals The Cult of Rakdos (B/R)",
			"is guild symbol originals The Gruul Clans (R/G)",
			"is guild symbol originals The Selesnya Conclave (G/W)",
			"is guild symbol originals The Orzhov Syndicate (W/B)",
			"is guild symbol originals The Izzet (U/R)",
			"is guild symbol originals The Golgari (B/G)",
			"is guild symbol originals The Boros Legion (R/W)",
			"is guild symbol originals The Simic Combine (G/U)",
			"is guild symbol ancients The Azorius Senate (W/U)",
			"is guild symbol ancients House Dimir (U/B)",
			"is guild symbol ancients The Cult of Rakdos (B/R)",
			"is guild symbol ancients The Gruul Clans (R/G)",
			"is guild symbol ancients The Selesnya Conclave (G/W)",
			"is guild symbol ancients The Orzhov Syndicate (W/B)",
			"is guild symbol ancients The Izzet (U/R)",
			"is guild symbol ancients The Golgari (B/G)",
			"is guild symbol ancients The Boros Legion (R/W)",
			"is guild symbol ancients The Simic Combine (G/U)",
			"is faction symbol mirrodin",
			"is faction symbol phyrexia",
			"is clan symbol The Abzan Houses (WBG)",
			"is clan symbol The Jeskai Way (URW)",
			"is clan symbol The Sultai Brood (BGU)",
			"is clan symbol The Mardu Horde (RWB)",
			"is clan symbol The Temur Frontier (GUR)",
			"is brood symbol Dromoka's Brood (GW)",
			"is brood symbol Ojutai's Brood (WU)",
			"is brood symbol Silumgar's Brood (UB)",
			"is brood symbol Kolaghan's Brood (BR)",
			"is brood symbol Atarka's Brood (RG)",
			"is unstable factions Order of the Widget",
			"is unstable factions Agents of S.N.E.A.K.",
			"is unstable factions League of Dastardly Doom",
			"is unstable factions Goblin Explosioneers",
			"is unstable factions Crossbreed Labs",
			"is colored xander hybrid mana B/R",
			"is colored xander hybrid mana U/B",
			"is colored xander hybrid mana B/G",
			"is colored xander hybrid mana R/G",
			"is colored xander hybrid mana G/U",
			"is colored xander hybrid mana U/R",
			"is colored xander hybrid mana W/B",
			"is colored xander hybrid mana G/W",
			"is colored xander hybrid mana R/W",
			"is colored xander hybrid mana W/U",
			"is xander hybrid mana B/R",
			"is xander hybrid mana U/B",
			"is xander hybrid mana B/G",
			"is xander hybrid mana R/G",
			"is xander hybrid mana G/U",
			"is xander hybrid mana U/R",
			"is xander hybrid mana W/B",
			"is xander hybrid mana G/W",
			"is xander hybrid mana R/W",
			"is xander hybrid mana W/U",
			"is future sight type symbols artifact",
			"is future sight type symbols creature",
			"is future sight type symbols enchantment",
			"is future sight type symbols instant",
			"is future sight type symbols land",
			"is future sight type symbols multiple",
			"is future sight type symbols planeswalker",
			"is future sight type symbols sorcery",
			"is other magic symbols aetherprint",
			"is other magic symbols chaos symbol",
			"is other magic symbols color pie",
			"is other magic symbols conspiracy stamp",
			"is other magic symbols story spotlight",
			"is other magic symbols color spotlight",
			"is other magic symbols jace consortium",
			"is other magic symbols phyrexia",
			"is other magic symbols seekers of carmot",
			"is other magic symbols the thran",
			"is other magic symbols foretell",
			"is other magic symbols innistrad provinces stensia",
			"is other magic symbols innistrad provinces kessig",
			"is other magic symbols innistrad provinces gavony",
			"is other magic symbols innistrad provinces nephalia",
			"is other magic symbols theros poleis akros",
			"is other magic symbols theros poleis meletis",
			"is other magic symbols theros poleis setessa",
			"is alara symbols Bant",
			"is alara symbols Esper",
			"is alara symbols Grixis",
			"is alara symbols Jund",
			"is alara symbols Naya",
			"is alara symbols colored Bant",
			"is alara symbols colored Esper",
			"is alara symbols colored Grixis",
			"is alara symbols colored Jund",
			"is alara symbols colored Naya",
			"is college symbols Silverquill",
			"is college symbols Prismari",
			"is college symbols Witherbloom",
			"is college symbols Lorehold",
			"is college symbols Quandrix",
			"is custom watermark one",
			"is custom watermark two",
			"is custom watermark three",
			"is custom watermark four",
			"is custom watermark five",
			"is custom watermark six",
			"is custom watermark seven",
			"is custom watermark eight",
			"is custom watermark nine",
			"is custom watermark ten",
			"is custom watermark card",
			"is set symbol"
		)));
		put("If the card's STAMP", new ArrayList<String>(Arrays.asList(
			"is none",
			"is standard",
			"is acorn",
			"is universes beyond",
			"is alchemy",
			"is alchemy old",
			"is heart",
			"is custom"
		)));
		put("If the card's ARTIST", defaultTextConditionOptions);
		put("If the card's TEMPLATE", new ArrayList<String>(Arrays.asList(
			"is the following template :"
		)));
		put("If the card's BORDER", new ArrayList<String>(Arrays.asList(
			"is black",
			"is white",
			"is grey",
			"is gold",
			"is the following R;G;B color :",
			"is not the following R;G;B color :"
		)));
		put("If the card's NOTES", defaultTextConditionOptions);
	}};
	
	public static Map<String, List<String>> followingConditionOptions = new LinkedHashMap<String, List<String>>() 
	{
		private static final long serialVersionUID = 1L;
	{
		
		for (String key : conditionOptions.keySet())
		{
			
			put("And i" + key.substring(1), conditionOptions.get(key));
			
		}
	}};
	
	public static Map<String, BiPredicate<String, String>> conditionOptionsMap = new LinkedHashMap<String, BiPredicate<String, String>>() 
	{
		private static final long serialVersionUID = 1L;
	{
		put("is the following :", conditionOperationEquals);
		put("is not the following :", conditionOperationNotEquals);
		put("contains the following :", conditionOperationContains);
		put("does not contain the following :", conditionOperationNotContains);
		put("is greater than the following number :", conditionOperationNumberGreater);
		put("is smaller than the following number :", conditionOperationNumberSmaller);
		put("is black", conditionOperationColorWordEquals);
		put("is white", conditionOperationColorWordEquals);
		put("is grey", conditionOperationColorWordEquals);
		put("is gold", conditionOperationColorWordEquals);
		put("is the following type :", conditionOperationTypeEquals);
		put("is not the following type :", conditionOperationTypeNotEquals);
		put("is the following super type :", conditionOperationSuperTypeEquals);
		put("is not the following super type :", conditionOperationSuperTypeNotEquals);
		put("is the following template :", conditionOperationTemplateEquals);
		put("is the following R;G;B color :", conditionOperationColorEquals);
		put("is not the following R;G;B color :", conditionOperationColorNotEquals);
	}};
	
	

	public static BiFunction<String, String, String> replacementOperationReplace = (a, b) -> a;
	
	public static BiFunction<String, String, String> replacementOperationAppend = (a, b) -> b + a;
	
	public static BiFunction<String, String, String> replacementOperationPrepend = (a, b) -> a + b;
	
	public static BiFunction<String, String, String> replacementOperationRemove = (a, b) -> b.replace(a, "").replace("  ", " ");
	
	public static BiFunction<String, String, String> replacementOperationNumberAdd = (a, b) -> String.valueOf(parseInt(b).intValue() + parseInt(a).intValue());
	
	public static BiFunction<String, String, String> replacementOperationNumberSubtract = (a, b) -> String.valueOf(parseInt(b).intValue() - parseInt(a).intValue());
	
	public static BiFunction<String, String, String> replacementOperationColorReplace = (a, b) ->
	{
		
		String[] rgbAsked = a.trim().replace("rgb", "").replace("(", "").replace(")", "").split(" *[.,:;] *");
		
		return "rgb(" + rgbAsked[0] + "," + rgbAsked[1] + "," + rgbAsked[2] + ")";
		
	};
	
	public static BiFunction<String, String, String> replacementOperationColorWordReplace = (a, b) -> colorWordsMap.get(a.replaceAll("^to ", ""));
	
	public static BiFunction<String, String, String> replacementOperationSuperTypeReplace = (a, b) ->
	{
		
		for (int i = 0; i < superTypeList.size(); i++)
		{
			
			b = b.replace(superTypeList.get(i), "");
			
		}
		
		return a + " " + removeTags(b).trim().replace("  ", " ");
		
	};
	
	public static BiFunction<String, String, String> replacementOperationTypeReplace = (a, b) ->
	{
		
		for (int i = 0; i < typeList.size(); i++)
		{
			
			b = b.replace(typeList.get(i), "");
			
		}
		
		return removeTags(b).trim().replace("  ", " ") + " " + a;
		
	};
	
	public static BiFunction<String, String, String> replacementOperationTemplateReplace = (a, b) -> a.replaceAll("^magic-", "").replaceAll("\\.mse-style$", "").toLowerCase();
	
	public static BiFunction<String, String, String> replacementOperationOtherReplace = (a, b) -> a.replaceAll("^to ", "");
	
	public static List<String> defaultTextReplacementOptions = new ArrayList<String>(Arrays.asList(
		"to be the following :",
		"by prepending the following :",
		"by appending the following :",
		"by removing the following :"
	));
	
	public static List<String> defaultNumberReplacementOptions = new ArrayList<String>(Arrays.asList(
		"to be the following :",
		"by adding the following number :",
		"by subtracting the following number :"
	));
	
	public static Map<String, List<String>> replacementOptions = new LinkedHashMap<String, List<String>>() 
	{
		private static final long serialVersionUID = 1L;
	{
		put("Change the card's NAME", defaultTextReplacementOptions);
		put("Change the card's CASTING COST", defaultTextReplacementOptions);
		put("Change the card's SUPER TYPE", new ArrayList<String>(Arrays.asList(
			"to be the following super type :",
			"by prepending the following :",
			"by removing the following :"
		)));
		put("Change the card's TYPE", new ArrayList<String>(Arrays.asList(
			"to be the following type :",
			"by appending the following :",
			"by removing the following :"
		)));
		put("Change the card's SUB TYPE", defaultTextReplacementOptions);
		put("Change the card's POWER", defaultNumberReplacementOptions);
		put("Change the card's TOUGHNESS", defaultNumberReplacementOptions);
		put("Change the card's LOYALTY", defaultNumberReplacementOptions);
		put("Change the card's RARITY", new ArrayList<String>(Arrays.asList(
			"to basic land",
			"to common",
			"to uncommon",
			"to rare",
			"to mythic rare",
			"to masterpiece",
			"to special"
		)));
		put("Change the card's RULE TEXT", defaultTextReplacementOptions);
		put("Change the card's FLAVOR TEXT", defaultTextReplacementOptions);
		put("Change the card's WATERMARK", new ArrayList<String>(Arrays.asList(
			"to none",
			"to mana symbol white",
			"to mana symbol blue",
			"to mana symbol black",
			"to mana symbol red",
			"to mana symbol green",
			"to mana symbol colorless",
			"to mana symbol snow",
			"to transparent mana symbol white",
			"to transparent mana symbol blue",
			"to transparent mana symbol black",
			"to transparent mana symbol red",
			"to transparent mana symbol green",
			"to guild symbol The Azorius Senate (W/U)",
			"to guild symbol House Dimir (U/B)",
			"to guild symbol The Cult of Rakdos (B/R)",
			"to guild symbol The Gruul Clans (R/G)",
			"to guild symbol The Selesnya Conclave (G/W)",
			"to guild symbol The Orzhov Syndicate (W/B)",
			"to guild symbol The Izzet (U/R)",
			"to guild symbol The Golgari (B/G)",
			"to guild symbol The Boros Legion (R/W)",
			"to guild symbol The Simic Combine (G/U)",
			"to guild symbol originals The Azorius Senate (W/U)",
			"to guild symbol originals House Dimir (U/B)",
			"to guild symbol originals The Cult of Rakdos (B/R)",
			"to guild symbol originals The Gruul Clans (R/G)",
			"to guild symbol originals The Selesnya Conclave (G/W)",
			"to guild symbol originals The Orzhov Syndicate (W/B)",
			"to guild symbol originals The Izzet (U/R)",
			"to guild symbol originals The Golgari (B/G)",
			"to guild symbol originals The Boros Legion (R/W)",
			"to guild symbol originals The Simic Combine (G/U)",
			"to guild symbol ancients The Azorius Senate (W/U)",
			"to guild symbol ancients House Dimir (U/B)",
			"to guild symbol ancients The Cult of Rakdos (B/R)",
			"to guild symbol ancients The Gruul Clans (R/G)",
			"to guild symbol ancients The Selesnya Conclave (G/W)",
			"to guild symbol ancients The Orzhov Syndicate (W/B)",
			"to guild symbol ancients The Izzet (U/R)",
			"to guild symbol ancients The Golgari (B/G)",
			"to guild symbol ancients The Boros Legion (R/W)",
			"to guild symbol ancients The Simic Combine (G/U)",
			"to faction symbol mirrodin",
			"to faction symbol phyrexia",
			"to clan symbol The Abzan Houses (WBG)",
			"to clan symbol The Jeskai Way (URW)",
			"to clan symbol The Sultai Brood (BGU)",
			"to clan symbol The Mardu Horde (RWB)",
			"to clan symbol The Temur Frontier (GUR)",
			"to brood symbol Dromoka's Brood (GW)",
			"to brood symbol Ojutai's Brood (WU)",
			"to brood symbol Silumgar's Brood (UB)",
			"to brood symbol Kolaghan's Brood (BR)",
			"to brood symbol Atarka's Brood (RG)",
			"to unstable factions Order of the Widget",
			"to unstable factions Agents of S.N.E.A.K.",
			"to unstable factions League of Dastardly Doom",
			"to unstable factions Goblin Explosioneers",
			"to unstable factions Crossbreed Labs",
			"to colored xander hybrid mana B/R",
			"to colored xander hybrid mana U/B",
			"to colored xander hybrid mana B/G",
			"to colored xander hybrid mana R/G",
			"to colored xander hybrid mana G/U",
			"to colored xander hybrid mana U/R",
			"to colored xander hybrid mana W/B",
			"to colored xander hybrid mana G/W",
			"to colored xander hybrid mana R/W",
			"to colored xander hybrid mana W/U",
			"to xander hybrid mana B/R",
			"to xander hybrid mana U/B",
			"to xander hybrid mana B/G",
			"to xander hybrid mana R/G",
			"to xander hybrid mana G/U",
			"to xander hybrid mana U/R",
			"to xander hybrid mana W/B",
			"to xander hybrid mana G/W",
			"to xander hybrid mana R/W",
			"to xander hybrid mana W/U",
			"to future sight type symbols artifact",
			"to future sight type symbols creature",
			"to future sight type symbols enchantment",
			"to future sight type symbols instant",
			"to future sight type symbols land",
			"to future sight type symbols multiple",
			"to future sight type symbols planeswalker",
			"to future sight type symbols sorcery",
			"to other magic symbols aetherprint",
			"to other magic symbols chaos symbol",
			"to other magic symbols color pie",
			"to other magic symbols conspiracy stamp",
			"to other magic symbols story spotlight",
			"to other magic symbols color spotlight",
			"to other magic symbols jace consortium",
			"to other magic symbols phyrexia",
			"to other magic symbols seekers of carmot",
			"to other magic symbols the thran",
			"to other magic symbols foretell",
			"to other magic symbols innistrad provinces stensia",
			"to other magic symbols innistrad provinces kessig",
			"to other magic symbols innistrad provinces gavony",
			"to other magic symbols innistrad provinces nephalia",
			"to other magic symbols theros poleis akros",
			"to other magic symbols theros poleis meletis",
			"to other magic symbols theros poleis setessa",
			"to alara symbols Bant",
			"to alara symbols Esper",
			"to alara symbols Grixis",
			"to alara symbols Jund",
			"to alara symbols Naya",
			"to alara symbols colored Bant",
			"to alara symbols colored Esper",
			"to alara symbols colored Grixis",
			"to alara symbols colored Jund",
			"to alara symbols colored Naya",
			"to college symbols Silverquill",
			"to college symbols Prismari",
			"to college symbols Witherbloom",
			"to college symbols Lorehold",
			"to college symbols Quandrix",
			"to custom watermark one",
			"to custom watermark two",
			"to custom watermark three",
			"to custom watermark four",
			"to custom watermark five",
			"to custom watermark six",
			"to custom watermark seven",
			"to custom watermark eight",
			"to custom watermark nine",
			"to custom watermark ten",
			"to custom watermark card",
			"to set symbol"
		)));
		put("Change the card's STAMP", new ArrayList<String>(Arrays.asList(
			"to none",
			"to standard",
			"to acorn",
			"to universes beyond",
			"to alchemy",
			"to alchemy old",
			"to heart",
			"to custom"
		)));
		put("Change the card's ARTIST", defaultTextReplacementOptions);
		put("Change the card's TEMPLATE", new ArrayList<String>(Arrays.asList(
			"to the following template :"
		)));
		put("Change the card's BORDER", new ArrayList<String>(Arrays.asList(
			"to black",
			"to white",
			"to grey",
			"to gold",
			"to the following R;G;B color :"
		)));
		put("Change the card's NOTES", defaultTextReplacementOptions);
		put("Delete the card", new ArrayList<String>());
	}};
	
	public static  Map<String, BiFunction<String, String, String>> replacementOptionsMap = new LinkedHashMap<String, BiFunction<String, String, String>>() 
	{
		private static final long serialVersionUID = 1L;
	{
		put("to be the following :", replacementOperationReplace);
		put("by prepending the following :", replacementOperationPrepend);
		put("by appending the following :", replacementOperationAppend);
		put("by removing the following :", replacementOperationRemove);
		put("by adding the following number :", replacementOperationNumberAdd);
		put("by subtracting the following number :", replacementOperationNumberSubtract);
		put("to black", replacementOperationColorWordReplace);
		put("to white", replacementOperationColorWordReplace);
		put("to grey", replacementOperationColorWordReplace);
		put("to gold", replacementOperationColorWordReplace);
		put("to be the following super type :", replacementOperationSuperTypeReplace);
		put("to be the following type :", replacementOperationTypeReplace);
		put("to the following template :", replacementOperationTemplateReplace);
		put("to the following R;G;B color :", replacementOperationColorReplace);
	}};
	
	
	
	public static boolean logToConsole = false;
	
	public static boolean packWhenLogging = true;
	
	
	
	
	
	public static void main(String[] args) throws Exception
	{
		
		logToConsole = true;
		
		
		
		String loadPath = System.getProperty("user.home") + "\\Desktop\\SETNAME.mse-set";
		
		
		
		String conditionCountString = "2";
		
		
		
		List<String> conditionKeyStrings = new ArrayList<String>(Arrays.asList(
			"If the card's RARITY",
			"And if the card's CASTING COST"
		));
		
		List<String> conditionOperationStrings = new ArrayList<String>(Arrays.asList(
			"is common",
			"contains the following :"
		));
		
		List<String> conditionValueStrings = new ArrayList<String>(Arrays.asList(
			"rare",
			"W"
		));
		
		
		
		String replacementKeyString = "Change the card's NAME";
		
		String replacementOperationString = "by prepending the following :";
		
		String replacementValueString = "A-";
		
		
		
		modify
		(
			loadPath,
			conditionCountString,
			conditionKeyStrings, conditionOperationStrings, conditionValueStrings,
			replacementKeyString, replacementOperationString, replacementValueString
		);
		
	}
	
	
	
	public static void modify
	(
		String loadPathString,
		String conditionCountString,
		List<String> conditionKeyStrings, List<String> conditionOperationStrings, List<String> conditionValueStrings,
		String replacementKeyString, String replacementOperationString, String replacementValueString)
	{
		
		System.out.println(new Date().toString().substring(11, 20) + "  INFO:    Starting.");
		
		packWhenLogging = true;
		
		
		
		//Load Set
		loadPath = loadPathString.endsWith(".mse-set") ? loadPathString : loadPathString + ".mse-set";
		
		backupPath = loadPath.substring(0, loadPath.length()-8) + " backup " + GUI.getDate() + ".mse-set";
		
		boolean loadSuccess = loadZipFile();
		
		if (!loadSuccess) return;
		
		
		
		//Get default template
		for (int i = 0; i < headers.size(); i++)
		{
			
			if (headers.get(i).startsWith("stylesheet:"))
			{
				
				defaultMap.put("	stylesheet:", headers.get(i).replaceAll("^stylesheet:", "").trim());
				
				break;
				
			}
		}
		
		if (defaultMap.get("	stylesheet:").equals("")) log("Could not find default Set template.", Color.red, "load");
		
		
		
		//Parse condition keys and operations
		int conditionCount = Integer.parseInt(conditionCountString);
		
		List<String> conditionKeys = new ArrayList<String>(conditionCount);
		
		List<BiPredicate<String, String>> conditionOperations = new ArrayList<BiPredicate<String, String>>(conditionCount);
		
		for (int k = 0; k < conditionCount; k++)
		{
			
			String conditionKey = keyMap.get(conditionKeyStrings.get(k).replaceAll("^(And i|I)f the card's ", ""));
			
			if (conditionKey == null) System.out.println(new Date().toString().substring(11, 20) + " ERROR:   Could not find condition key ' " + conditionKeyStrings.get(k).replaceAll("^(And i|I)f the card's ", "") + " '.");
			
			conditionKeys.add(conditionKey);
			
			
			
			BiPredicate<String, String> conditionOperation = conditionOptionsMap.get(conditionOperationStrings.get(k));
			
			if (conditionOperation == null)
			{
				
				System.out.println(new Date().toString().substring(11, 20) + " ERROR:   Could not find condition operation ' " + conditionOperationStrings.get(k) + " '.");
				
				conditionValueStrings.set(k, conditionOperationStrings.get(k));
				
				conditionOperation = conditionOperationOtherEquals;
				
			}
			
			conditionOperations.add(conditionOperation);
			
		}
		
		
		
		//Parse replacement key and operation
		boolean mustDelete = replacementKeyString.equals("Delete the card.");
		
		String replacementKey = null;
		
		BiFunction<String, String, String> replacementOperation = null;
		
		if (!mustDelete)
		{
			
			replacementKey = keyMap.get(replacementKeyString.replace("Change the card's ", ""));
			
			if (replacementKey == null) System.out.println(new Date().toString().substring(11, 20) + " ERROR:   Could not find replacement key ' " + replacementKeyString.replace("Change the card's ", "") + " '.");
			
			replacementOperation = replacementOptionsMap.get(replacementOperationString);
			
			if (replacementOperation == null)
			{
				
				System.out.println(new Date().toString().substring(11, 20) + " ERROR:   Could not find replacement operation ' " + replacementOperationString + " '.");
				
				replacementValueString = replacementOperationString;
				
				replacementOperation = replacementOperationOtherReplace;
				
			}
		}
		
		
		
		
		//Start looping on the cards
		int cardCount = cards.size();
		
		int cardModifiedCount = 0;
		
		cardLoop:
		for (int i = 0; i < cards.size(); i++)
		{
			
			List<String> card = cards.get(i);
			
			
			
			//Check the conditions
			for (int k = 0; k < conditionCount; k++)
			{
				
				String conditionKey = conditionKeys.get(k);
				
				BiPredicate<String, String> conditionOperation = conditionOperations.get(k);
				
				String conditionValueString = conditionValueStrings.get(k);
				
				String conditionField = getFieldValue(card, conditionKey);
				
				
				System.out.println(conditionValueString);
				System.out.println(conditionField);
				System.out.println();
				if (!conditionOperation.test(conditionValueString, conditionField)) continue cardLoop;
				
			}
			
			
			
			//Modify the card
			cardModifiedCount++;
			
			if (mustDelete)
			{
				
				cards.remove(i);
				
				i--;
				
			}
			
			else
			{
				
				int replacementIndex = getFieldIndex(card, replacementKey);
				
				
				
				String replacementField = replacementIndex == -1 ? defaultMap.get(replacementKey) : getFieldValue(card, replacementIndex, replacementKey);
				
				replacementField = replacementOperation.apply(replacementValueString, replacementField);
				
				
				
				if (replacementIndex == -1)
				{
					
					 addField(card, replacementKey, replacementField);
					
				}
				
				else
				{
					
					deleteField(card, replacementIndex, replacementKey);
					
					addField(card, replacementKey, replacementField);
					
				}
			}
		}
		
		
		
		//Log results
		String cardCountString = cardCount + " card" + (cardCount != 1 ? "s" : "") + " found, ";
		
		String cardModifiedCountString = cardModifiedCount + " card" + (cardModifiedCount != 1 ? "s" : "") + (mustDelete ? " deleted." : " modified.");
		
		log(cardCountString + cardModifiedCountString, Color.black, "logic");
		
		
		
		//Save Set
		saveZipFile();
		
		
		
		System.gc();
		
		System.out.println(new Date().toString().substring(11, 20) + "  INFO:    Done.");
		
	}
	
	
	
	public static int getFieldIndex(List<String> card, String key)
	{
		
		if (!key.startsWith("	")) key = "	" + key;
		
		if (!key.endsWith(":")) key = key + ":";
		
		for (int j = 0; j < card.size(); j++)
		{
			
			if (card.get(j).startsWith(key)) return j;
			
		}
		
		return -1;
		
	}
	
	
	
	public static String getFieldValue(List<String> card, int row, String key)
	{
		
		if (!key.startsWith("	")) key = "	" + key;
		
		if (!key.endsWith(":")) key = key + ":";
		
		String value = card.get(row).replace(key, "").replaceAll("^ ", "");
		
		row++;
		
		String continuation = "";
		
		while (row < card.size())
		{
			
			if (card.get(row).startsWith("		"))
			{
				
				continuation = continuation + "\n" + card.get(row).substring(2);
				
				row++;
				
			}
			
			else break;
			
		}
		
		return value + continuation;
		
	}
	
	public static String getFieldValue(List<String> card, String key)
	{
		
		int index = getFieldIndex(card, key);
		
		if (index == -1)
		{
			
			return defaultMap.get(key);
			
		}
		
		else return getFieldValue(card, index, key);
		
	}
	
	
	
	public static void addField(List<String> card, String key, String value)
	{
		
		String[] valueLines = value.split("\n");
		
		card.add(key + " " + valueLines[0]);
		
		for (int i = 1; i < valueLines.length; i++)
		{
			
			card.add("\t\t" + valueLines[i]);
			
		}
	}
	
	
	
	public static void deleteField(List<String> card, int row, String key)
	{
		
		if (!card.get(row).startsWith(key))
		{
			
			System.out.println(new Date().toString().substring(11, 20) + " ERROR:   Trying to delete field at the wrong line.");
			
			return;
			
		}
		
		card.remove(row);
		
		while (row < card.size() && card.get(row).startsWith("		"))
		{
			
			card.remove(row);
			
		}
	}
	
	
	
	//public static int deleteCard(List<String> setFile, int row)
	//{
	//	
	//	int deletedLinesCount = 0;
	//	
	//	while (row < setFile.size() && setFile.get(row).startsWith("	"))
	//	{
	//		
	//		setFile.remove(row);
	//		
	//		deletedLinesCount++;
	//		
	//	}
	//	
	//	if (row > 0)
	//	{
	//		
	//		while (setFile.get(row-1).startsWith("	"))
	//		{
	//			
	//			setFile.remove(row-1);
	//			
	//			deletedLinesCount++;
	//			
	//		}
	//		
	//		if (!setFile.get(row-1).startsWith("card:"))
	//		{
	//			
	//			log("Error while deleting card.", Color.red, "logic");
	//			
	//		}
	//		
	//		else
	//		{
	//			
	//			setFile.remove(row-1);
	//			
	//			deletedLinesCount++;
	//			
	//		}
	//	}
	//	
	//	return deletedLinesCount;
	//	
	//}
	
	
	
	public static String removeTags(String string)
	{
		
		return string.replaceAll("<[^>]+>", "");
		
	}
	
	
	
	public static boolean loadZipFile()
	{
		
		headers.clear();
		
		cards.clear();
		
		List<String> setLines = null;
		
		
		
		try
		{
			
			File zipFile = new File(loadPath);
			
			if (!zipFile.exists())
			{
				
				log("No Set file found at specified path.", Color.red, "load");
				
				return false;
				
			}
			
			
			
			Path zipFilePath = Paths.get(loadPath);
			
			try (FileSystem fileSystem = FileSystems.newFileSystem(zipFilePath, (ClassLoader)null))
			{
				
				Path setPath = fileSystem.getPath("/set");
				
				setLines = Files.readAllLines(setPath, StandardCharsets.UTF_8);
				
			}
			
			log("Set file loaded.", Color.black, "load");
			
		}
		
		catch (Exception e)
		{
			
			log("Cannot parse contents of Set file.", Color.red, "load");
			
			return false;
			
		}
		
		
		
		int setLinesCount = setLines.size();
		
		for (int i = 0; i < setLinesCount;)
		{
			
			if (setLines.get(i).equals("card:"))
			{
				
				List<String> card = new ArrayList<String>();
				
				do
				{
					
					card.add(setLines.get(i));
					
					i++;
					
				} while(i < setLinesCount && setLines.get(i).startsWith("	"));
				
				cards.add(card);
				
			}
			
			else
			{
				
				do
				{
					
					headers.add(setLines.get(i));
					
					i++;
					
				} while(i < setLinesCount && setLines.get(i).startsWith("	"));
			}
		}
		
		return true;
		
	}
	
	
	
	public static boolean saveZipFile()
	{
		
		try
		{
			
			Files.copy(Paths.get(loadPath), Paths.get(backupPath));
			
			File backUpFile = new File(backupPath);
			
			if (!backUpFile.exists())
			{
				
				log("Unable to create backup, aborting. (Try closing MSE.)", Color.red, "save");
				
				log("", Color.black, "saveName");
				
				return false;
				
			}
			
			else
			{
				
				log("Backup file created at ' " + backupPath + " '.", Color.black, "saveName");
				
			}
			
			
			
			List<String> setLines = new ArrayList<String>(headers);
			
			for (int i = 0; i < cards.size(); i++)
			{
				
				setLines.addAll(cards.get(i));
				
			}
			
			
			
			Path zipFilePath = Paths.get(loadPath);
			
			try (FileSystem fileSystem = FileSystems.newFileSystem(zipFilePath, (ClassLoader)null))
			{
				
				Path setPath = fileSystem.getPath("/set");
				
				Files.write(setPath, setLines, StandardCharsets.UTF_8);
				
			}
			
			log("Set file saved.", Color.black, "save");
			
		} catch (Exception e)
		{
			
			log("Unable to save Set file at specified path.", Color.red, "save");
			
			return false;
			
		}
		
		return true;
		
	}
	
	
	
	public static Integer parseInt(String text)
	{
		
		try
		{
			
			return Integer.parseInt(text.trim());
			
		}
		
		catch (Exception e)
		{
			
			log("Could not parse ' " + text + " ' as a number.", Color.red, "logic");
			
			return null;
			
		}
	}
	
	
	
	public static void log(String text, Color color, String out)
	{
		
		boolean textIsEmpty = text == null || text.equals("");
		
		if (logToConsole)
		{
			
			if (textIsEmpty) return;
			
			System.out.println(new Date().toString().substring(11, 20) + (color == Color.black ? "  INFO:    " : "  ERROR:   " ) + text);
			
		}
		
		else
		{
			
			if (textIsEmpty)
			{
				
				text = "";
				
				color = Color.black;
				
			}
			
			if (out.equals("load"))
			{
				
				if (!color.equals(Color.red) && Launcher.loadMessage.getForeground().equals(Color.red)) return;
				
				Launcher.loadMessage.setText(text);
				
				Launcher.loadMessage.setForeground(color);
				
			}
			
			else if (out.equals("logic"))
			{
				
				if (!color.equals(Color.red) && Launcher.logicMessage.getForeground().equals(Color.red)) return;
				
				Launcher.logicMessage.setText(text);
				
				Launcher.logicMessage.setForeground(color);
				
			}
			
			else if (out.equals("save"))
			{
				
				if (!color.equals(Color.red) && Launcher.saveMessage.getForeground().equals(Color.red)) return;
				
				Launcher.saveMessage.setText(text);
				
				Launcher.saveMessage.setForeground(color);
				
			}
			

			else
			{
				
				if (!color.equals(Color.red) && Launcher.saveNameMessage.getForeground().equals(Color.red)) return;
				
				Launcher.saveNameMessage.setText(text);
				
				Launcher.saveNameMessage.setForeground(color);
				
			}
			
			
			
			if (color.equals(Color.red) || packWhenLogging) Launcher.window.pack();
			
		}
	}
}



class ModifierWorker extends SwingWorker<Integer, Integer>
{
	
	String loadPathString;
	
	String conditionCountString;
	
	List<String> conditionKeyStrings;
	
	List<String> conditionOperationStrings;
	
	List<String> conditionValueStrings;
	
	String replacementKeyString;
	
	String replacementOperationString;
	
	String replacementValueString;
	
	
	
	ModifierWorker(String loadPathString, String conditionCountString, List<String> conditionKeyStrings, List<String> conditionOperationStrings, List<String> conditionValueStrings, String replacementKeyString, String replacementOperationString, String replacementValueString)
	{
		
		this.loadPathString = loadPathString;
		
		this.conditionCountString = conditionCountString;
		
		this.conditionKeyStrings = conditionKeyStrings;
		
		this.conditionOperationStrings = conditionOperationStrings;
		
		this.conditionValueStrings = conditionValueStrings;
		
		this.replacementKeyString = replacementKeyString;
		
		this.replacementOperationString = replacementOperationString;
		
		this.replacementValueString = replacementValueString;
		
	}
	
	@Override
	protected Integer doInBackground()
	{
		
		Modifier.modify(loadPathString, conditionCountString, conditionKeyStrings, conditionOperationStrings, conditionValueStrings, replacementKeyString, replacementOperationString, replacementValueString);
		
		return 0;
		
	}
}
