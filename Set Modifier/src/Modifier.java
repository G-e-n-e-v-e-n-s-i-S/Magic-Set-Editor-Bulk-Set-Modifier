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
import java.util.function.BooleanSupplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.SwingWorker;



public class Modifier
{
	
	
	
	public static String loadPath;
	
	public static String backupPath;
	
	public static List<String> headers = new ArrayList<String>();
	
	public static List<List<String>> cards = new ArrayList<List<String>>();
	
	

	public static List<String> currentCard;
	
	public static int currentCardIndex;
	
	
	
	public static String currentConditionKey;
	
	public static BooleanSupplier currentConditionOperation;
	
	public static String currentConditionValue;
	
	public static String currentConditionField;
	
	
	
	public static String currentReplacementKey;
	
	public static Runnable currentReplacementOperation;
	
	public static String currentReplacementValue;
	
	public static String currentReplacementField;
	
	
	
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
		put("MANA VALUE",	"	cmc:");
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
		put("	cmc:",			"0");
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
	
	
	
	public static BooleanSupplier conditionOperationEquals = () -> currentConditionValue.trim().equals(currentConditionField.trim()) || currentConditionValue.trim().equals(removeTags(currentConditionField).trim());
	
	public static BooleanSupplier conditionOperationNotEquals = () -> !conditionOperationEquals.getAsBoolean();
	
	public static BooleanSupplier conditionOperationContains = () -> currentConditionField.contains(currentConditionValue.trim()) || removeTags(currentConditionField).contains(currentConditionValue.trim());
	
	public static BooleanSupplier conditionOperationNotContains = () -> !conditionOperationContains.getAsBoolean();
	
	public static BooleanSupplier conditionOperationNumberGreater = () -> currentConditionField.equals("") ? false : parseInt(currentConditionValue).intValue() < parseInt(currentConditionField).intValue();
	
	public static BooleanSupplier conditionOperationNumberSmaller = () -> currentConditionField.equals("") ? false : parseInt(currentConditionValue).intValue() > parseInt(currentConditionField).intValue();
	
	public static BooleanSupplier conditionOperationColorEquals = () ->
	{
		
		String[] splitValue = currentConditionValue.trim().replace("rgb", "").replace("(", "").replace(")", "").split(" *[.,:;] *");
		
		String[] splitField = currentConditionField.trim().replace("rgb", "").replace("(", "").replace(")", "").split(" *[.,:;] *");
		
		return splitValue[0].equals(splitField[0]) && splitValue[1].equals(splitField[1]) && splitValue[2].equals(splitField[2]);
		
	};
	
	public static BooleanSupplier conditionOperationColorNotEquals = () -> !conditionOperationColorEquals.getAsBoolean();
	
	public static BooleanSupplier conditionOperationColorWordEquals = () ->
	{
		
		currentConditionValue = colorWordsMap.get(currentConditionValue.replaceAll("^is ", ""));
		
		return conditionOperationColorEquals.getAsBoolean();
		
	};
	
	public static BooleanSupplier conditionOperationTypeEquals = () ->
	{
		
		for (int i = 0; i < superTypeList.size(); i++)
		{
			
			currentConditionField = currentConditionField.replace(superTypeList.get(i), "");
			
		}
		
		currentConditionField = removeTags(currentConditionField).trim().replace("  ", " ");
		
		return currentConditionValue.trim().equals(currentConditionField);
		
	};
	
	public static BooleanSupplier conditionOperationTypeNotEquals = () -> !conditionOperationTypeEquals.getAsBoolean();
	
	public static BooleanSupplier conditionOperationSuperTypeEquals = () ->
	{
		
		for (int i = 0; i < typeList.size(); i++)
		{
			
			currentConditionField = currentConditionField.replace(typeList.get(i), "");
			
		}
		
		currentConditionField = removeTags(currentConditionField).trim().replace("  ", " ");
		
		return currentConditionValue.trim().equals(currentConditionField);
		
	};
	
	public static BooleanSupplier conditionOperationSuperTypeNotEquals = () -> !conditionOperationSuperTypeEquals.getAsBoolean();
	
	public static BooleanSupplier conditionOperationTemplateEquals = () -> currentConditionValue.replaceAll("^magic-", "").replaceAll("\\.mse-style$", "").toLowerCase().equals(currentConditionField);
	
	public static BooleanSupplier conditionOperationManaValueEquals = () ->
	{
		
		String manaValue = String.valueOf(Math.round(getManaValue(currentCard)));
		
		return currentConditionValue.equals(manaValue);
		
	};
	
	public static BooleanSupplier conditionOperationManaValueNotEquals = () -> !conditionOperationManaValueEquals.getAsBoolean();
	
	public static BooleanSupplier conditionOperationManaValueGreater = () ->
	{
		
		int manaValue = (int)Math.round(getManaValue(currentCard));
		
		return parseInt(currentConditionValue).intValue() < manaValue;
		
	};
	
	public static BooleanSupplier conditionOperationManaValueSmaller = () ->
	{
		
		int manaValue = (int)Math.round(getManaValue(currentCard));
		
		return parseInt(currentConditionValue).intValue() > manaValue;
		
	};
	
	public static BooleanSupplier conditionOperationOtherEquals = () ->
	{
		
		currentConditionValue = currentConditionValue.replaceAll("^is ", "");
		
		return conditionOperationEquals.getAsBoolean();
		
	};
	
	public static List<String> defaultTextConditionOptions = new ArrayList<String>(Arrays.asList(
		"is exactly the following :",
		"is not the following :",
		"contains the following :",
		"does not contain the following :"
	));
	
	public static List<String> defaultNumberConditionOptions = new ArrayList<String>(Arrays.asList(
		"is exactly the following :",
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
		put("If the card's MANA VALUE", new ArrayList<String>(Arrays.asList(
			"is exactly the following : ",				//These end with spaces to distiguish them from defaultNumberConditionOptions
			"is not the following : ",					//These end with spaces to distiguish them from defaultNumberConditionOptions
			"is greater than the following number : ",	//These end with spaces to distiguish them from defaultNumberConditionOptions
			"is smaller than the following number : "	//These end with spaces to distiguish them from defaultNumberConditionOptions
		)));
		put("If the card's SUPER TYPE", new ArrayList<String>(Arrays.asList(
			"is exactly the following super type :",
			"is not the following super type :",
			"contains the following :",
			"does not contain the following :"
		)));
		put("If the card's TYPE", new ArrayList<String>(Arrays.asList(
			"is exactly the following type :",
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
			"is exactly the following (folder name) :"
		)));
		put("If the card's BORDER", new ArrayList<String>(Arrays.asList(
			"is black",
			"is white",
			"is grey",
			"is gold",
			"is exactly the following R;G;B color :",
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
	
	public static Map<String, BooleanSupplier> conditionOptionsMap = new LinkedHashMap<String, BooleanSupplier>() 
	{
		private static final long serialVersionUID = 1L;
	{
		put("is exactly the following :", conditionOperationEquals);
		put("is not the following :", conditionOperationNotEquals);
		put("contains the following :", conditionOperationContains);
		put("does not contain the following :", conditionOperationNotContains);
		put("is greater than the following number :", conditionOperationNumberGreater);
		put("is smaller than the following number :", conditionOperationNumberSmaller);
		put("is exactly the following : ", conditionOperationManaValueEquals);				//These end with spaces to distiguish them from defaultNumberConditionOptions
		put("is not the following : ", conditionOperationManaValueNotEquals);				//These end with spaces to distiguish them from defaultNumberConditionOptions
		put("is greater than the following number : ", conditionOperationManaValueGreater);	//These end with spaces to distiguish them from defaultNumberConditionOptions
		put("is smaller than the following number : ", conditionOperationManaValueSmaller);	//These end with spaces to distiguish them from defaultNumberConditionOptions
		put("is black", conditionOperationColorWordEquals);
		put("is white", conditionOperationColorWordEquals);
		put("is grey", conditionOperationColorWordEquals);
		put("is gold", conditionOperationColorWordEquals);
		put("is exactly the following type :", conditionOperationTypeEquals);
		put("is not the following type :", conditionOperationTypeNotEquals);
		put("is exactly the following super type :", conditionOperationSuperTypeEquals);
		put("is not the following super type :", conditionOperationSuperTypeNotEquals);
		put("is exactly the following (folder name) :", conditionOperationTemplateEquals);
		put("is exactly the following R;G;B color :", conditionOperationColorEquals);
		put("is not the following R;G;B color :", conditionOperationColorNotEquals);
	}};
	
	

	public static Runnable replacementOperationDeleteCard = () ->
	{
		
		cards.remove(currentCardIndex);
		
		currentCardIndex--;
		
	};
	
	public static Runnable replacementOperationReplace = () -> addField(currentCard, currentReplacementKey, currentReplacementValue);
	
	public static Runnable replacementOperationAppend = () -> addField(currentCard, currentReplacementKey, currentReplacementField + currentReplacementValue);
	
	public static Runnable replacementOperationPrepend = () -> addField(currentCard, currentReplacementKey, currentReplacementValue + currentReplacementField);
	
	public static Runnable replacementOperationRemove = () -> addField(currentCard, currentReplacementKey, currentReplacementField.replace(currentReplacementValue, "").replace("  ", " "));
	
	public static Runnable replacementOperationNumberAdd = () ->
	{
		
		int fieldValue = currentReplacementField.trim().equals("") ? 0 : parseInt(currentReplacementField).intValue();
		
		addField(currentCard, currentReplacementKey, String.valueOf(fieldValue + parseInt(currentReplacementValue).intValue()));
		
	};
	
	public static Runnable replacementOperationNumberSubtract = () ->
	{
		
		int fieldValue = currentReplacementField.trim().equals("") ? 0 : parseInt(currentReplacementField).intValue();
		
		addField(currentCard, currentReplacementKey, String.valueOf(fieldValue - parseInt(currentReplacementValue).intValue()));
		
	};
	
	public static Runnable replacementOperationColorReplace = () ->
	{
		
		String[] splitValue = currentReplacementValue.trim().replace("rgb", "").replace("(", "").replace(")", "").split(" *[.,:;] *");
		
		String cleanedValue = "rgb(" + splitValue[0] + "," + splitValue[1] + "," + splitValue[2] + ")";
		
		addField(currentCard, currentReplacementKey, cleanedValue);
		
	};
	
	public static Runnable replacementOperationColorWordReplace = () -> addField(currentCard, currentReplacementKey, colorWordsMap.get(currentReplacementValue.replaceAll("^to ", "")));
	
	public static Runnable replacementOperationSuperTypeReplace = () ->
	{
		
		for (int i = 0; i < superTypeList.size(); i++)
		{
			
			currentReplacementField = currentReplacementField.replace(superTypeList.get(i), "");
			
		}
		
		addField(currentCard, currentReplacementKey, currentReplacementValue + " " + removeTags(currentReplacementField).trim().replace("  ", " "));
		
	};
	
	public static Runnable replacementOperationTypeReplace = () ->
	{
		
		for (int i = 0; i < typeList.size(); i++)
		{
			
			currentReplacementField = currentReplacementField.replace(typeList.get(i), "");
			
		}
		
		addField(currentCard, currentReplacementKey, removeTags(currentReplacementField).trim().replace("  ", " ") + " " + currentReplacementValue);
		
	};
	
	public static Runnable replacementOperationTemplateReplace = () ->
	{
		
		deleteField(currentCard, "	stylesheet_version:");
		
		addField(currentCard, currentReplacementKey, currentReplacementValue.replaceAll("^magic-", "").replaceAll("\\.mse-style$", "").toLowerCase());
		
	};
	
	public static Runnable replacementOperationOtherReplace = () -> addField(currentCard, currentReplacementKey, currentReplacementValue.replaceAll("^to ", ""));
	
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
			"to the following (folder name) :"
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
	
	public static  Map<String, Runnable> replacementOptionsMap = new LinkedHashMap<String, Runnable>() 
	{
		private static final long serialVersionUID = 1L;
	{
		put("Delete the card", replacementOperationDeleteCard);
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
		put("to the following (folder name) :", replacementOperationTemplateReplace);
		put("to the following R;G;B color :", replacementOperationColorReplace);
	}};
	
	
	
	public static boolean logToConsole = false;
	
	public static boolean packWhenLogging = true;
	
	
	
	
	
	//public static void main(String[] args) throws Exception
	//{
	//	
	//	logToConsole = true;
	//	
	//	
	//	
	//	String loadPath = System.getProperty("user.home") + "\\Desktop\\SETNAME.mse-set";
	//	
	//	
	//	
	//	String conditionCountString = "2";
	//	
	//	
	//	
	//	List<String> conditionKeyStrings = new ArrayList<String>(Arrays.asList(
	//		"If the card's BORDER",
	//		"And if the card's CASTING COST"
	//	));
	//	
	//	List<String> conditionOperationStrings = new ArrayList<String>(Arrays.asList(
	//		"is black",
	//		"contains the following :"
	//	));
	//	
	//	List<String> conditionValueStrings = new ArrayList<String>(Arrays.asList(
	//		"rare",
	//		"W"
	//	));
	//	
	//	
	//	
	//	String replacementKeyString = "Delete the card";
	//	
	//	String replacementOperationString = "by prepending the following :";
	//	
	//	String replacementValueString = "A-";
	//	
	//	
	//	
	//	modify
	//	(
	//		loadPath,
	//		conditionCountString,
	//		conditionKeyStrings, conditionOperationStrings, conditionValueStrings,
	//		replacementKeyString, replacementOperationString, replacementValueString
	//	);
	//	
	//}
	
	
	
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
		
		List<BooleanSupplier> conditionOperations = new ArrayList<BooleanSupplier>(conditionCount);
		
		List<String> conditionValues = new ArrayList<String>(conditionCount);
		
		for (int k = 0; k < conditionCount; k++)
		{
			
			String conditionKey = keyMap.get(conditionKeyStrings.get(k).replaceAll("^(And i|I)f the card's ", ""));
			
			if (conditionKey == null) System.out.println(new Date().toString().substring(11, 20) + " ERROR:   Could not find condition key ' " + conditionKeyStrings.get(k).replaceAll("^(And i|I)f the card's ", "") + " '.");
			
			conditionKeys.add(conditionKey);
			
			
			
			BooleanSupplier conditionOperation = conditionOptionsMap.get(conditionOperationStrings.get(k));
			
			if (conditionOperation == null)
			{
				
				System.out.println(new Date().toString().substring(11, 20) + " ERROR:   Could not find condition operation ' " + conditionOperationStrings.get(k) + " '.");
				
				conditionOperations.add(conditionOperationOtherEquals);
				
				conditionValues.add(conditionOperationStrings.get(k));
				
			}
			
			else
			{
				
				conditionOperations.add(conditionOperation);
				
				if (conditionOperationStrings.get(k).matches(".*: ?$"))
				{
					
					conditionValues.add(conditionValueStrings.get(k));
					
				}
				
				else
				{
					
					conditionValues.add(conditionOperationStrings.get(k));
					
				}
			}
		}
		
		
		
		//Parse replacement key and operation
		currentReplacementKey = null;
		
		currentReplacementKey = keyMap.get(replacementKeyString.replace("Change the card's ", ""));
		
		if (currentReplacementKey == null) System.out.println(new Date().toString().substring(11, 20) + " ERROR:   Could not find replacement key ' " + replacementKeyString.replace("Change the card's ", "") + " '.");
		
		
		
		currentReplacementOperation = null;
		
		currentReplacementOperation = replacementOptionsMap.get(replacementKeyString);
		
		if (currentReplacementOperation == null) currentReplacementOperation = replacementOptionsMap.get(replacementOperationString);
		
		if (currentReplacementOperation == null)
		{
			
			System.out.println(new Date().toString().substring(11, 20) + " ERROR:   Could not find replacement operation ' " + replacementOperationString + " '.");
			
			currentReplacementValue = replacementOperationString;
			
			currentReplacementOperation = replacementOperationOtherReplace;
			
		}
		
		
		
		if (replacementOperationString.matches(".*: ?$"))
		{
			
			currentReplacementValue = replacementValueString;
			
		}
		
		else
		{
			
			currentReplacementValue = replacementOperationString;
			
		}
		
		
		
		//Start looping on the cards
		int cardCount = cards.size();
		
		int cardModifiedCount = 0;
		
		cardLoop:
		for (currentCardIndex = 0; currentCardIndex < cards.size(); currentCardIndex++)
		{
			
			currentCard = cards.get(currentCardIndex);
			
			
			
			//Check the conditions
			for (int k = 0; k < conditionCount; k++)
			{
				
				currentConditionKey = conditionKeys.get(k);
				
				currentConditionOperation = conditionOperations.get(k);
				
				currentConditionValue = conditionValues.get(k);
				
				currentConditionField = getFieldValue(currentCard, currentConditionKey);
				
				
				
				if (!currentConditionOperation.getAsBoolean()) continue cardLoop;
				
			}
			
			
			
			//Modify the card
			cardModifiedCount++;
			
			currentReplacementField = getFieldValue(currentCard, currentReplacementKey);
			
			deleteField(currentCard, currentReplacementKey);
			
			currentReplacementOperation.run();
			
		}
		
		
		
		//Log results
		String cardCountString = cardCount + " card" + (cardCount != 1 ? "s" : "") + " found, ";
		
		String cardModifiedCountString = cardModifiedCount + " card" + (cardModifiedCount != 1 ? "s" : "") + (replacementKeyString.equals("Delete the card") ? " deleted." : " modified.");
		
		log(cardCountString + cardModifiedCountString, Color.black, "logic");
		
		
		
		//Save Set
		saveZipFile();
		
		
		
		System.gc();
		
		System.out.println(new Date().toString().substring(11, 20) + "  INFO:    Done.");
		
	}
	
	
	
	public static int getFieldIndex(List<String> card, String key)
	{
		
		if (key == null) return -1;
		
		
		
		if (!key.startsWith("	")) key = "	" + key;
		
		if (!key.endsWith(":")) key = key + ":";
		
		for (int j = 0; j < card.size(); j++)
		{
			
			if (card.get(j).startsWith(key)) return j;
			
		}
		
		return -1;
		
	}
	
	
	
	public static String getFieldValue(List<String> card, String key)
	{
		
		int row = getFieldIndex(card, key);
		
		if (row == -1)
		{
			
			return defaultMap.get(key);
			
		}
		
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
	
	
	
	public static void addField(List<String> card, String key, String value)
	{
		
		String[] valueLines = value.split("\n");
		
		card.add(key + " " + valueLines[0]);
		
		for (int i = 1; i < valueLines.length; i++)
		{
			
			card.add("\t\t" + valueLines[i]);
			
		}
	}
	
	
	
	public static void deleteField(List<String> card, String key)
	{
		
		int row = getFieldIndex(card, key);
		
		if (row == -1) return;
		
		card.remove(row);
		
		while (row < card.size() && card.get(row).startsWith("		"))
		{
			
			card.remove(row);
			
		}
	}
	
	
	
	public static double getManaValue(List<String> card)
	{
		
		String manaCost = removeTags(getFieldValue(card, "	casting_cost:"));
		
		double manaValue = 0;
		
		
		
		manaCost = manaCost.replaceAll("(TK|E)/(?![0-9])", "");
		
		manaCost = manaCost.replaceAll("(?<![0-9])/(TK|E)", "");
		
		manaCost = manaCost.replaceAll("(?<!/)(TK|E)(?!/)", "");
		
		
		
		int length = manaCost.length();
		
		manaCost = manaCost.replace("1/2", "");
		
		if (length != manaCost.length()) manaValue += 0.5d;
		
		
		
		Matcher halfManaMatcher = Pattern.compile("\\|[WUBRGCVLHSEA]").matcher(manaCost);
		
		while (halfManaMatcher.find())
		{
			
			manaValue += 0.5;
			
		}
		
		manaCost = manaCost.replaceAll("\\|[WUBRGCVLHSEA]", "");
		
		
		
		Matcher numberHybridManaMatcher = Pattern.compile("([0-9])(/[WUBRGCVLHSEA]){1,4}").matcher(manaCost);
		
		while (numberHybridManaMatcher.find())
		{
			
			try
			{
				
				manaValue += Integer.parseInt(numberHybridManaMatcher.group(1));
				
			} catch (Exception e) {}
		}
		
		manaCost = manaCost.replaceAll("([0-9])(/[WUBRGCVLHSEA]){1,4}", "");
		
		
		
		Matcher colorManaMatcher = Pattern.compile("([WUBRGCVLHSEA])(/[WUBRGCVLHSEA]){0,4}").matcher(manaCost);
		
		while (colorManaMatcher.find())
		{
			
			manaValue++;
			
		}
		
		manaCost = manaCost.replaceAll("([WUBRGCVLHSEA])(/[WUBRGCVLHSEA]){0,4}", "");
		
		
		
		Matcher numberManaMatcher = Pattern.compile("[0-9]+").matcher(manaCost);
		
		while (numberManaMatcher.find())
		{
			
			try
			{
				
				manaValue += Integer.parseInt(numberManaMatcher.group(0));
				
			} catch (Exception e) {}
		}
		
		
		
		return manaValue;
		
	}
	
	
	
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
				
				Files.delete(setPath);
				
				Files.write(setPath, setLines, StandardCharsets.UTF_8);
				
			}
			
			log("Set file saved.", Color.black, "save");
			
		} catch (Exception e)
		{
			
			log("Unable to save Set file at specified path. (Try closing MSE.)", Color.red, "save");
			
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
