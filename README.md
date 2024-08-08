# Conversation Plugin

> Minecraft plugin for version 1.21.1

1. [**Information**](#1-information)
2. [**How to use**](#2-how-to-use)
3. [**JSON Structure**](#3-json-structure)
4. [**Types**](#4-types)
5. [**Command Interface**](#5-command-interface)

## 1. Information

The **Conversation Plugin** (spigot link will be added), in conjunction with the **Conversation Web Editor** (link will be added when the website is finished) and the [Command Interface](#5-command-interface), offers a flexible framework for developing interactive dialogues and quests within Minecraft.

Conversations are designed on the **Conversation Web Editor** (link will be added when the website is finished), where developers can customize dialogues and narrative pathways. Once configured, these conversations are exported as JSON files and placed within the `conversations` directory inside the server's `plugin` folder. Through the [Command Interface](#5-command-interface), developers gain further control, allowing them to add players, remove players, and manipulate the conversation flow dynamically, making the system adaptable to various in-game scenarios.

Refer to the [How To Use](#2-how-to-use) for more information.

### Conversations

For a player to participate in a conversation, they must first be added to it. This process is handled through the [Command Interface](#5-command-interface). It is important to note that some commands only position the player at a node within the conversation but do not activate it. A trigger is required to initiate a node. Various trigger types are implemented to facilitate this, including time-based, block-based, item-based, or entity-based triggers, as well as direct command triggers that can only be activated via the [Command Interface](#5-command-interface). Additionally, the conversation’s identifier is derived from the conversation file’s name, excluding the *.json* extension, which serves as its unique reference within the system and means conversations are not allowed to have the same name.

### Conversation Settings

Each conversation includes several configurable options aimed at improving the player experience and conveying relevant information about the conversation itself. Among these options, developers can enable notifications, such as sending messages or playing sound to signal the start or end of a conversation, enhancing clarity for the player.

An important setting within this configuration is the ***blocking flag***, which is generally recommended to be set to true in the majority of conversations. This flag prevents nodes from overlapping or interfering with nodes from the same or different conversations, maintaining a coherent flow in scenarios where the player is involved. Disabling this flag can interrupt the intended narrative sequence, and should only be done in cases where supplementary text from supporting characters is desired to enhance the game environment’s realism and to breathe life into the game world. Notably, while the blocking flag prevents interference between blocking nodes, it does not restrict a player from engaging in multiple blocking conversations simultaneously. Additionally, non-blocking nodes will execute even if the player is already engaged in a blocking node and can be run simultaneously. If you encounter a problem with a blocking node quick relog should solve the issue.

Furthermore, the ***citizens*** flag in conversation settings can be turned on, to replace the player's model with a [Citizens plugin](https://www.spigotmc.org/resources/citizens.13811/) NPC. This provides a better, more pleasant experience during cutscenes.

### Players

Each player must first be added to the conversation before they can participate in it. This is achieved with commands within the [Command Interface](#5-command-interface). Commands can either directly run the node or position the player at the node. When a player is positioned at the node, it needs to be activated with a trigger or a command, allowing developers to control the initiation timing of a node for each player. With these triggers and commands, developers have management tools to guide player's progress in conversations.

A player can simultaneously participate in multiple conversations. He can also have multiple active nodes with the active ***blocking*** flag, however, it is recommended to manage this carefully, as concurrent nodes may disrupt the conversation flow. This is managed by the blocking flag in [Conversation Settings](#conversation-settings), which can help organize overlapping interactions.

The player has a list of requirements that are implemented as a list of specific strings or keywords that a node may require to be activated. To advance to such nodes, players must possess all necessary string tokens/requirements. These tokens can only be assigned with the help of commands, giving developers flexibility in managing progression conditions within conversations.

Additionally, players can input responses through an input node, with each response stored as a variable within a map of variable-value pairs. These variables are enclosed in percentage signs (e.g., %variable%), allowing developers to insert customized player-specific details into dialogue text. Such variables can also be initialized and modified through the Command Interface, enabling more immersive interactions and storytelling.

### Messages or Text Sent to The Player and Commands

Text displayed to the player supports [Minecraft color and formatting codes](https://minecraft.fandom.com/wiki/Formatting_codes), enabling developers to add visual customization and distinction to the dialogue or the information presented. Beyond basic formatting, the text undergoes a preprocessing stage before being sent to the player, during which it substitutes any defined variables the player possesses. To designate a word as a variable, it must be enclosed within two percentage signs (e.g., %variable%). Variables are obtained using an Input Node, which captures player-specific input for future use in the conversation dialogues. Furthermore, variables can be added or modified with the [Command Interface](#5-command-interface).

This variable replacement functionality extends to commands within the nodes as well. When a command references a variable, it will replace the placeholder with the player’s corresponding variable value, enhancing the flexibility of automated command execution. Additionally, the keyword *%player%* is reserved for automatic substitution with the player’s name. This reserved keyword is particularly valuable for cases where the player’s name is required to execute a command, this removes the need for a dedicated Input Node solely to capture it.

### Nodes for Structuring Conversation Flow

Nodes are the fundamental components that structure the conversation flow within the plugin. Each node has its unique identifier and is designed to represent an individual dialogue text within the conversation. There are several types of nodes, each meant to deliver and present specific types of messages, instructions, information, or choices to the player. These nodes vary in how they present dialogue or information, broadly divided into three main display methods: text in the chat, text on the player’s screen, and text embedded within the game world itself.

Each node in the conversation structure includes specific settings related to its type, a reference to the next node (except Choice Nodes), a trigger mechanism that determines how or when the node is activated, commands, and requirements.

The requirements for each node act as access conditions, preventing the player from progressing to a particular node if any of the specified criteria are unmet. Developers can manage these requirements by removing or assigning them to the player with commands, thus determining the player's progression path through the conversation based on predefined conditions. Requirements are implemented as a list of strings/words, each representing a specific condition or "token" that the player must possess to access the node. Without matching all the necessary tokens, the player cannot proceed to the designated node.

Additionally, each node can contain multiple commands that are executed either at the beginning or the end of the node, with an option for them to be executed after a time delay. Commands can be configured to run either from the player's perspective or via the console, adding flexibility for managing various aspects of gameplay. Leveraging these commands through the Command Interface enables developers to link conversations, add requirements to the player, move the player to other nodes, automate conversation flow, and implement a wide range of actions in-game, providing extensive control over the player’s interaction and experience.

#### Chat-Based Nodes

Chat-based nodes utilize the in-game chat as a way of presenting storytelling or information to the player. Chat can be also used as a way to receive a player's input and unlike other nodes, chat nodes do not rely on duration, allowing the player to control the pacing of the conversation and proceed at his own speed.

> **Chat Node**
The Chat Node allows simple text messages to be sent directly to the player’s chat. This node does not impose any delay, meaning the player can proceed to the next node instantly after receiving the message, maintaining a smooth conversational pace.

> **Input Node**
The Input Node is an interactive node that captures information from the player. When triggered, this node listens for the player’s input in the chat and stores it in a specified variable, which is subsequently saved to the player’s map of variable values in the conversation file. The stored input can then be referenced in later dialogues. Additionally, the Input Node can send a prompt or question to guide the player in providing the desired information. If left empty, no text will appear. The node remains active indefinitely until the player submits a response, thereby pausing the conversation flow until an input is provided.

#### Screen-Based Nodes

Screen-based nodes provide visual feedback directly on the player’s screen, enabling more immersive storytelling options. These nodes are effective for conveying messages that need to remain in view for a specific duration or provide progress indicators during quests or tasks.

> **Boss Bar Node**
This node displays a message in the player’s boss bar, which is located at the top of the screen alongside a progress bar. The Boss Bar Node is particularly effective for visually indicating a player's progress in a story, task, or quest, as the fullness of the progress bar can be adjusted to reflect the player’s progression. The text and progress bar remains visible for the node's specified duration.

> **Action Bar Node**
The Action Bar Node displays text directly above the player’s hot bar, which resides at the lower portion of the screen. This positioning is ideal for presenting brief, supplementary text, such as internal thoughts, language subtitles, or off-screen character dialogue. The displayed text persists for the specified duration, after which the Action Bar fades away.

> **Title and Subtitle Nodes**
These nodes display text in the center of the player’s screen, drawing significant attention to the message. You can display only the title, only the subtitle, or both at the same time. This type of node is well-suited for scene transitions, area introductions, story explanations, internal thoughts, or narrative highlights that require prominent on-screen messaging. This node is shown for a specified time duration and you can adjust its fade-in and fade-out timer to your liking.

#### World-Based Text Display Nodes

World-based display nodes present text directly within the game world, creating a more immersive storytelling and interactive experience by allowing the text to appear in a spatial context relative to the player. This feature allows for more dynamic storytelling, as the text can be tied to entities or specified locations within the game world. 

These nodes offer extensive text customization options, including adjustments to line width, text orientation, glow effects, background color, text alignment, and more. These features allow developers to tailor the appearance of the text to suit the game's aesthetic and vibe. Additionally, text can be made visible only to the player interacting with the node, ensuring that individual gameplay experiences are preserved and unaffected by other players' interactions.

> **Display Node**
The Display Node utilizes a text display entity to spawn text directly into the game world. This text can appear at a specific location or be bound to an entity, enabling dynamic movement alongside it. The text lasts for a predetermined duration, after which it disappears. 
>
>The unique feature of the Display Node is its ability to transition into cutscenes. With cutscenes, users can set parameters, such as the location of the camera that lays on a radius around the targeted location or entity. Height, position, direction, target focus, and zoom level, among other things, can be adjusted. This feature has several modes to customize the player's experience:
> - ***No Cutscene***: The text is displayed at the specified location or bound to an entity, and the player retains full body and camera movement control, unaffected by any camera adjustments.
> - ***Player Mode***: In this mode, the text is displayed at the specified location or bound to an entity, however, the player’s camera is directed towards a selected target, their movement is restricted, and the camera zooms in on the target for capturing player's focus, resulting in a first-person cutscene.
> - **Cutscene Mode**: This immersive mode displays the text at the specified location or bound to an entity and places the player in the spectator mode, teleports him to a designated location, forces their camera to look at the target, and applies a zoom effect, which results in a third person cutscene. If the ***citizens*** flag is enabled in the [Conversation Settings](#conversation-settings), the player’s character is replaced by an NPC with the exact location and camera direction before teleporting the player, enhancing authenticity. Additionally, cutscenes can be smoothly chained, where all subsequent Display Nodes in cutscene mode with a [Time Trigger](#time-trigger) will seamlessly transition, creating a fluid cinematic experience.

> **Choice Node**
The Choice Node enables branching narratives by presenting players with multiple interactive options. This node also uses a text display entity to show choices within the game world. The main message of the Choice Node can be positioned at a specified location or attached to an entity, providing means to ask a question or whatever information is needed. This text can also stay empty and only the choices will be displayed. Each choice within this node is rendered as an interactive text option, residing either at a specified location or attached to an entity, and the plugin calculates the spacing and locations of all choices and evenly places them between the entity/location and the player for more pleasing visual storytelling. Players can select their preferred path by left-clicking on one of these options, which then directs them to the chosen branch of the conversation. Choice can have specified requirements, when the player does not possess those requirements, the choice won't be displayed, allowing for better conversation flow control. The Choice Node remains active until the player makes a selection, enabling decision-based gameplay and more complex narrative structures.

In sum, the various node types provide many methods for structuring and displaying dialogues, choices, and information and getting player's input within the game environment. By combining chat-based, screen-based, and world-based nodes, developers can create multifaceted and engaging narrative experiences, flexible to the player’s actions and context. Each node type serves a distinct purpose, enabling flexible and immersive storytelling that adapts to both the player’s interactions and the surrounding game world.

### Trigger Mechanisms for Node Activation

In this plugin, triggers serve as the primary mechanisms for initiating nodes and orchestrating the flow of a conversation. Several straightforward triggers are pre-defined within the plugin to support basic functionality. These triggers facilitate the smooth transition and progression through the various stages of the conversation. Among the most essential triggers you will use are:

[Time Trigger](#time-trigger): This trigger activates a node following a specified time delay. It is particularly useful for pacing conversations or introducing a pause before progressing to the next dialogue or action.

[Location Trigger](#location-trigger): This trigger initiates a node when a player enters a predefined radius around a specified location. This spatial awareness can add a layer of immersion, allowing the conversation to adapt dynamically as the player navigates the game environment.

[Command Trigger](#command-trigger): This trigger offers a high degree of flexibility, allowing activation based on specific commands. The Command Trigger can be initiated at any chosen moment, which makes it a valuable tool for customizing conversation flow. This adaptability allows developers to integrate the conversation plugin seamlessly with custom plugins, command blocks, or other in-game mechanics.

In addition to these primary triggers, the system includes other types that respond to basic interactions with items, blocks, and entities. These triggers further extend the functionality, allowing for varied and responsive conversational experiences within the game. For comprehensive details, please refer to the [Trigger Object](#trigger-object), which provides an overview of all available triggers and their configurations.

This modular design enables the conversation plugin to accommodate diverse interaction patterns, enhancing both the interactivity and immersion of in-game dialogues.

### Conversation Tool

You can use the [Conversation Tool](#give-tool-command) command to receive a modified item inside the game. This tool aids developers in incorporating accurate locations and entity identifiers into their conversation structures.

When the player right-clicks on a block while holding this item, the tool will output the precise coordinates of that block into the chat. This feature includes a convenient option for players to copy the displayed coordinates directly, making the process of documenting locations for use in conversations or quests a little bit more convenient.

Similarly, if the player right-clicks on an entity using this tool, the unique identifier (UUID) of that entity will be presented in the chat. This display also features a copy option, enabling players to easily retrieve and utilize the UUID for further interaction within the conversation framework.

## 2. How To Use

The following steps outline the process of setting up and using the conversation plugin on your server:

**1. Download the Plugin**
> Begin by obtaining the desired version of the plugin.

**2. Add to Server Plugins Directory**
> Place the downloaded plugin file into the server's `plugins` directory. This is essential for the server to recognize and load the plugin.

**3. Initialize the Plugin**
> Start your server. On the first run, the plugin will automatically create a `conversations` directory within the `plugins` folder. You may also do this manually if you don't want to start the server yet.

**4. Create a Custom Conversation**
> Use the **Conversation Web Editor** (link will be added) to design your conversation. The conversation file will be exported as a JSON.

**5. Place the Conversation File**
> Move the exported JSON file into the `conversations` directory. This allows the plugin to access and recognize the conversation file when the server starts or reloads.

**6. Start the Server**
> Reload or start the server again. The plugin will now load the conversation file, making it available for the players.

**7. Manage Players through Commands**
> Control and manage players via the [Command Interface](#5-command-interface).

## 3. JSON Structure

The conversation name is derived directly from the file name, excluding the *.json* extension.

### Conversation Object

The main components of a conversation are contained within the Conversation Object. This object organizes the core elements necessary for defining, configuring, and managing the conversation flow.

```json
{
    "start_node": "NODE_ID",
    "settings": {},
    "players": {},
    "nodes": {}
}
```

- **start_node**: Represents the ID of the initial node where the conversation begins.
- **[settings](#settings-object)**: Configuration settings for the conversation.
- **[players](#players-object)**: Holds data for all players currently involved in the conversation.
- **[nodes](#nodes-object)**: Contains all the nodes that the conversation consists of.

### Settings Object

Configuration of the conversation.

```json
"settings": {
    "start_message": false,
    "end_message": false,
    "start_sound": false,
    "end_sound": false,
    "blocking": true,
    "citizens": true
}
```

- **start_message**: Determines if a message is sent to the player when the conversation begins.
- **end_message**: Determines if a message is sent to the player when the conversation ends.
- **start_sound**: Determines if a sound is played to the player when the conversation begins.
- **end_sound**: Determines if a sound is played to the player when the conversation ends.
- **blocking**: If set to true, this prevents all blocking nodes from initiating until the current node is completed, thereby maintaining a consistent flow. Setting this to false allows nodes to overlap, which can disrupt the interaction unless carefully managed.
- **citizens**: Enables support for the [Citizens Plugin](https://www.spigotmc.org/resources/citizens.13811/), enhancing player immersion during cutscenes.

### Camera Object

Defines camera settings for cutscenes. The camera can be set on a radius around the target. Distance and height can be multiplied, allowing developers to control player perspective.

```json
"camera_settings": {
    "cutscene": "NONE | CUTSCENE | PLAYER",
    "target": "PLAYER | ENTITY | MIDDLE",
    "angle": 0,
    "distance_multiplier": 1.0,
    "height_multiplier": 1.0,
    "zoom": 0
},
```

- [**camera**](#cutscene-type): Specifies cutscene behavior.
- [**target**](#camera-target): Where the camera is focused during the cutscene.
- **angle**: Specifies the camera’s angle, measured in degrees. Works with any whole number and uses trigonometry to calculate the position.
    - **0** - The camera faces to the east.
    - **90** - The camera faces to the north.
    - **180** - The camera faces to the south.
    - **270** - The camera faces to the west.
- **distance_multiplier**: Multiplier of the distance between the player and the entity.
- **height_multiplier**: Multiplier of the height of the player.
- **zoom**: Controls the zoom level of the camera, allowing developers to create more focused view within the cutscene context. In order to achieve this, slowness effect is applied.

### Players Object

Stores data for active players in the conversation. This information is essential for tracking individual player progress, requirements, and any custom variables set during the conversation.

```json
"players": {
    "PLAYER_NAME_1": {
        "current_node": "NODE_ID",
        "requirements": [
            "TOKEN_1",
            "TOKEN_2"
        ],
        "variables": {
            "%variable_1%": "VALUE_1",
            "%variable_2%": "VALUE_2"
        }
    },
    "PLAYER_NAME_2": {
        "current_node": "NODE_ID",
        "requirements": [
            "TOKEN_1",
        ],
        "variables": {}
    }
}
```

- **name**: The unique identifier for each player in the conversation, represented by their in-game name.
- **current_node**: The ID of the conversation node that the player is currently interacting with, used to track their position within the dialogue sequence.
- **requirements**: A list of requirements (in string format) necessary for the player to access certain nodes. This list ensures that players meet specific conditions before advancing.
- **variables**: Stores input data or variables unique to the player. These variables are used to personalize dialogue or to use in commands.

### Nodes Object

Holds all nodes in the conversation. Each node serves as one dialogue in the conversation. The last node in the dialogue path has to be ended with an empty string ("").

```json
"nodes": {
    "NODE_ID_1": {
        "node_type": "TITLE | SUBTITLE | TITLE_SUBTITLE | CHAT | DISPLAY | ACTION_BAR | BOSS_BAR | CHOICE | INPUT",
        "node_settings": {},
        "next_node": "NODE_ID_2",
        "trigger": {},
        "requirements": [
            "TOKEN_1",
            "TOKEN_2"
        ],
        "commands": []
    },
    "NODE_ID_2": {
        "node_type": "TITLE | SUBTITLE | TITLE_SUBTITLE | CHAT | DISPLAY | ACTION_BAR | BOSS_BAR | CHOICE | INPUT",
        "node_settings": {},
        "next_node": "",
        "trigger": {},
        "requirements": [
            "TOKEN_1"
        ],
        "commands": []
    }
}
```

- **[node_type](#node-types)**: Defines the type of node, determining the format and display style of the text.
- **[node_settings](#node-settings-object)**: Contains node specific settings, depending on its type.
- **next_node**: Specifies the ID of the next node to be accessed after the current node finishes, defining the flow of the conversation. In the case of Choice Node this field is set to "CHOICE" and has no impact on the conversation flow.
- **[trigger](#trigger-object)**: An object holding information about the conditions or actions required to initiate this node. Triggers can be time-based, event-based, or manually activated.
- **requirements**: A list of conditions or tokens that the player must fulfill to access this node, ensuring structured progress through the conversation.
- **[commands](#command-object)**: A list of command objects executed either at the start or the end of the node, often used to modify game elements, set variables, or control conversation flow.

### Command Object

Encapsulates the details necessary for executing specific commands within the node. Each command is structured as an object in an array, allowing multiple commands to be associated with a single node. The command itself needs to be written without the slash ('/') sign. The commands will be executed in order, however, "START" commands will be always executed before "END" commands. All marked variables (with two percentage signs, at the start and in the end) will be automatically replaced with existing player variables.

```json
"commands": [
    {
        "command": "example command",
        "execute": "START | END",
        "delay": 0,
        "sender": "PLAYER | CONSOLE"
    },
    {
        "command": "example command %player%",
        "execute": "START | END",
        "delay": 0,
        "sender": "PLAYER | CONSOLE"
    }
]
```

- **command**: Contains the command to be executed, specified without the preceding slash.
- [**execute**](#command-execution-type): Determines when will the command be executed.
- **delay**: Specifies a delay, in seconds, after which the command is executed.
- [**sender**](#command-sender-type): Indicates who is responsible for sending the command.

### Trigger Object

The Trigger Object is a crucial component in the conversation structure, defining the conditions under which a specific node will be activated. When the trigger requirements are met, and the player qualifies to enter the node, the node will be executed.

```json
"trigger": {
    "trigger_type": "BLOCK | ENTITY | ITEM | ELIMINATE | LOCATION | TIME | COMMAND | INTERACT",
}
```
- [**trigger_type**](#trigger-types): Specifies type of a trigger.

#### Block Trigger

Activates a conversation node when the player interacts with a block at a designated location. This allows for dynamic responses based on player actions within the game environment. There can be additional functionality added with the `"remove"` parameter, which will remove drop or block based on the action performed.

```json
"trigger": {
    "trigger_type": "BLOCK",
    "action": "INTERACT | BREAK | PLACE",
    "location": {"x": 0, "y": 0, "z": 0},
    "remove": false
}
```
- **[action](#block-action-type)**: Defines the type of action the player must perform with the block to activate the trigger.
- **location**: Specifies the exact coordinates of the block.
- **remove**: Controls if the block or its drop is removed after the action.
    - `"INTERACT"`: Removes the block in the location.
    - `"BREAK"`: Removes the block drop after the block in the specified location is broken.
    - `"PLACE"`: Removes the block after placing it in the specified location.

#### Entity Trigger

Activates a conversation node when the player interacts with an entity with specified unique identifier (UUID). This allows for events and dialogues to be contextually linked to the player's actions involving various entities.

```json
"trigger": {
    "trigger_type": "ENTITY",
    "action": "INTERACT | DAMAGE | ELIMINATE",
    "entity": "ENTITY_UUID"
}
```

- [**action**](#entity-action-type): Defines the specific type of interaction the player must perform with the entity to activate the trigger.
- **entity**: Identifier of the entity (UUID) that the player will interact with. This ID is essential for the trigger to correctly identify which entity's interactions will activate the node. You can use the [Conversation Tool](#conversation-tool) to get entity UUID.

#### Item Trigger

Activates the node based on specific player interaction with an item. The `"name"` parameter specifies either [Material](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html) type or a display name, which is a custom name given to the item with an anvil or a command. [Material](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html) type is case insensitive and display name is case sensitive when comparing the name.

```json
"trigger": {
    "trigger_type": "ITEM",
    "action": "PICKUP | DROP | USE",
    "name": "",
    "amount": 0,
    "consume": 0
}
```

- [**action**](#item-action-type): Defines the specific type of interaction the player must perform with the item to activate the trigger.
- **name**: This can either be the [Material](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html) type (case insensitive) or the custom display name of the item (case sensitive).
- **amount**: Required amount of items to activate the trigger.
- **consume**: Number of specified items consumed after activating the trigger.
    - `"PICKUP"`: Removes the amount from all specified items from player's inventory.
    - `"DROP"`: Consume does not work with this action.
    - `"USE"`: Removes the amount of specified items from a player's hand only.

#### Elimination Trigger

The Elimination Trigger activates a conversation node based on the player's actions of eliminating a specified number of mobs.

```json
"trigger": {
    "trigger_type": "ELIMINATE",
    "eliminate": "HOSTILE | PASSIVE | ANY",
    "quantity": 0
}
```

- [**eliminate**](#mob-type): Type of mob to eliminate.
- **quantity**: Number of mobs to eliminate.

#### Location Trigger

The Location Trigger is designed to activate a conversation node when a player enters a specified area defined by a center point and a radius. This trigger can be used to create events or dialogue that occur based on the player's movements within the game world.

```json
"trigger": {
    "trigger_type": "LOCATION",
    "location": {"x": 0, "y": 0, "z": 0},
    "radius": 0
}
```

- **location**: Location that defines the center of the trigger area.
- **radius**: This field sets the distance from the specified location that constitutes the trigger area. If the player comes within this radius, the associated conversation node will be triggered. The radius can be adjusted to suit the requirements of the game design, allowing for both small and large interaction zones.

#### Time Trigger

The Time Trigger activates a conversation node after a specified delay, allowing for timed events within the dialogue or gameplay flow.

```json
"trigger": {
    "trigger_type": "TIME",
    "delay": 0
}
```

- **delay**: Delay after which the node is activated.

#### Command Trigger

The Command Trigger allows a node to be activated solely through a command. This trigger is useful for scenarios where specific actions or states in the game need to precede the conversation, providing developers with flexibility in how dialogues are initiated.

```json
"trigger": {
    "trigger_type": "COMMAND"
}
```

#### Interact Trigger

The Interact Trigger activates a conversation node when the player interacts with anything in the game.

```json
"trigger": {
    "trigger_type": "INTERACT"
}
```

### Node Settings Object

The Node Settings Object defines the specific configuration for each type of node used in the conversation. Each node type has unique settings that determine how it behaves and displays information.

#### Title Node

Displays a title on the screen with specified animation effects.

```json
"node_settings": {
    "title": "TITLE",
    "fade_in": 0,
    "fade_out": 0,
    "duration": 0
}
```

- **title**: The main title text is displayed on the screen.
- **fade_in**: Time (in seconds) for the title to fade in.
- **fade_out**: Time (in seconds) for the title to fade out.
- **duration**: Time (in seconds) the title remains fully visible before fading out.

#### Subtitle Node

Displays a subtitle on the screen with specified animation effects.

```json
"node_settings": {
    "subtitle": "SUBTITLE",
    "fade_in": 0,
    "fade_out": 0,
    "duration": 0
}
```

- **subtitle**: The subtitle text is displayed on the screen.
- **fade_in**: Time (in seconds) for the title to fade in.
- **fade_out**: Time (in seconds) for the title to fade out.
- **duration**: Time (in seconds) the title remains fully visible before fading out.

#### Title Subtitle Node

Displays a title and a subtitle on the screen with specified animation effects.

```json
"node_settings": {
    "title": "TITLE",
    "subtitle": "SUBTITLE",
    "fade_in": 0,
    "fade_out": 0,
    "duration": 0
}
```

- **title**: The main title text is displayed on the screen.
- **subtitle**: The subtitle text is displayed on the screen.
- **fade_in**: Time (in seconds) for the title to fade in.
- **fade_out**: Time (in seconds) for the title to fade out.
- **duration**: Time (in seconds) the title remains fully visible before fading out.

#### Chat Node

Displays a message in the player's chat.

```json
"node_settings": {
    "text": "TEXT"
}
```

- **text**: The message displayed in the player's chat.

#### Display Node

Displays text at a specific location or attached to an entity for a set duration.

```json
"node_settings": {
    "text": "TEXT",
    "duration": 0,
    "target": "ENTITY | LOCATION",
    "location": {"x": 0, "y": 0, "z": 0},
    "entity": "ENTITY_UUID",
    "camera_settings": {},
    "display_settings": {}
}
```

- **text**: The message displayed.
- **duration**: Duration (in seconds) the text remains visible.
- [**target**](#display-target): Specifies whether the display is anchored to an entity or a location.
- **location**: Coordinates where the text is displayed (if the target is set to `LOCATION`).
- **entity**: UUID of the entity associated with the text (if the target is set to `ENTITY`).
- [**camera_settings**](#camera-object): Configuration for how the player's camera view during a cutscene.
- [**display_settings**](#display-settings-object): Appearance options for the text.

#### Action Bar Node

Displays a message in the player's action bar for a specified duration.

```json
"node_settings": {
    "text": "TEXT",
    "duration": 0
}
```

- **text**: The message is displayed in the action bar.
- **duration**: Time (in seconds) the action bar message is visible.

#### Boss Bar Node

Displays a boss bar with a progress indicator and text.

```json
"node_settings": {
    "text": "TEXT",
    "progress": 0,
    "duration": 0
}
```

- **text**: The message is displayed in the boss bar.
- **progress**: A value from 0.0 to 1.0 indicating the boss bar's progress (0.0 is empty, 1.0 is full).
- **duration**: Total time (in seconds) the boss bar is displayed.

#### Choice Node

Provides the player with a set of choices to select from.

```json
"node_settings": {
    "text": "TEXT",
    "target": "ENTITY | LOCATION",
    "location": {"x": 0, "y": 0, "z": 0},
    "entity": "ENTITY_UUID",
    "display_settings": {},
    "choices": []
}
```

- **text**: The prompt or text displayed to the player.
- [**target**](#display-target): Specifies whether the display is anchored to an entity or a location.
- **location**: Coordinates where the text is displayed (if the target is set to `LOCATION`).
- **entity**: UUID of the entity associated with the text (if the target is set to `ENTITY`).
- [**display_settings**](#display-settings-object): Appearance options for the text.
- [**choices**](#choices-array): Array of options the player can select.

#### Input Node

Prompts the player for input and stores the result in a variable.

```json
"node_settings": {
    "text": "TEXT",
    "variable": "VARIABLE"
}
```

- **text**: The message or prompt displayed to the player for input.
- **variable**: The name of the variable of the player's input.

### Display Settings Object

The Display Settings define how text is presented within the game world, allowing developers to customize the appearance and behavior of text displays.

```json
"display_settings": {
    "face": "VERTICAL | HORIZONTAL | CENTER",
    "shadow": false,
    "alignment": "CENTER | LEFT | RIGHT",
    "text_opacity": 0,
    "see_through": false,
    "visible": false,
    "glowing": false,
    "scale": 1.0,
    "background": 0,
    "line_width": 0,
    "bind_to_entity": "NONE | LOW_ELEVATION | MEDIUM_ELEVATION | HIGH_ELEVATION",
    "elevation": 0
}
```

- [**face**](#display-face-type): Determines the orientation of the text.
- **shadow**: Indicates whether the text display casts a shadow, enhancing its visual depth.
- [**alignment**](#text-alignment-type): Specifies how the text aligns.
- **text_opacity**: Controls the transparency of the text (0 is fully transparent, 1 is fully opaque).
- **see_through**: Allows text to be seen through objects.
- **visible**: Determines if the text is visible only for the player or everyone.
- **glowing**: Whether the text has a glowing effect.
- **scale**: Adjusts the size of the text (1 is normal size, greater than 1 increases size, less than 1 decreases size).
- **background**: The background color or style behind the text, can be transparent. (Uses ARGB)
- **line_width**: Specifies the maximum characters on each line of text before wrapping occurs.
- [**bind_to_entity**](#entity-bind-type): Specifies whether the text is bound to an entity and how high it will be displayed.
- **elevation**: How many blocks higher will the text appear when the text is not bound to an entity (start's at entity's height).

### Choices Array

The Choices Array contains the configuration for individual choice options presented to the player.

```json
"choices": [
    {
        "text": "TEXT",
        "display_settings": {},
        "target": "LOCATION | ENTITY",
        "location": {"x": 0, "y": 0, "z": 0},
        "requirements": [],
        "node": "NODE_ID"
    }
]
```

- **text**: The text displayed for the choice.
- [**target**](#display-target): Specifies whether the choice is displayed in the specified location or created automatically in front of the main target (location/entity).
- **location**: Coordinates where the choice text is displayed (if the target is set to `LOCATION`).
- [**display_settings**](#display-settings-object): Appearance options for the choice text.
- **requirements**: Conditions that must be met for this choice to be shown.
- **node**: The ID of the following node when the player selects this choice.

## 4. Types

### Node Types

Various node types.

```java
public enum NodeType {

    SUBTITLE,
    TITLE,
    TITLE_SUBTITLE,
    CHAT,
    DISPLAY,
    ACTION_BAR,
    BOSS_BAR,
    CHOICE,
    INPUT,
}
```

- `SUBTITLE`: Displays a subtitle message.
- `TITLE`: Displays a title message.
- `TITLE_SUBTITLE`: Displays a combined title and subtitle message.
- `CHAT`: Sends a message to the player's chat.
- `DISPLAY`: Shows a text display in the world at a specific location.
- `ACTION_BAR`: Sends a message to the player's action bar.
- `BOSS_BAR`: Shows a boss bar with a progress indicator.
- `CHOICE`: Presents choices to the player as a text display in the world, allows selection.
- `INPUT`: Sends a message to chat and waits for the player’s response.

### Trigger Types

Various trigger types.

```java
public enum TriggerType {

    LOCATION,
    INTERACT,
    ENTITY,
    BLOCK,
    ELIMINATE,
    ITEM,
    TIME,
    COMMAND,
}
```

- `LOCATION`: Triggers the node after the player enters a radius of the location.
- `INTERACT`: Triggers the node after the player interacts with something.
- `ENTITY`: Triggers the node after the player performs an action with the specified entity.
- `BLOCK`: Triggers the node after the player performs an action with a block in specified location..
- `ELIMINATE`: Triggers the node when the player eliminates specified number of mobs.
- `ITEM`: Triggers the node after the player performs an action with an item.
- `TIME`: Triggers the node after a time delay.
- `COMMAND`: Triggers the node with a command.

### Block Action Type

Action with the block.

 ```java
 public enum BlockActionType {

    INTERACT,
    PLACE,
    BREAK,
}
```

- `INTERACT`: Triggers the node when the player interacts with the block in the specified location.
- `PLACE`: Triggers the node when the player places a block in the specified location.
- `BREAK`: Triggers the node when the player breaks a block in the specified location.

### Entity Action Type

Action with the entity.

```java
public enum EntityActionType {

    INTERACT,
    DAMAGE,
    ELIMINATE,
}
```

- `INTERACT`: Triggers when player interacts with the specified entity.
- `DAMAGE`: Triggers when player damages the specified entity.
- `ELIMINATE`: Triggers when player eliminates the specified entity.

### Item Action Type

Action with the item.

```java
public enum ItemActionType {

    PICKUP,
    DROP,
    USE,
}
```

- `PICKUP`: Triggers when player picks up the item.
- `DROP`: Triggers when player drops the item.
- `USE`: Triggers when player uses the item.

### Mob Type

What type of a mob player must eliminate.

```java
public enum EliminationType {

    HOSTILE,
    PASSIVE,
    ANY,
}
```

- `HOSTILE`: Triggers when a player eliminates a specified number of hostile mobs.
- `PASSIVE`: Triggers when a player eliminates a specified number of passive mobs.
- `ANY`: Triggers when a player eliminates a specified number of both passive and hostile mobs.

### Entity Bind Type

Decides whether the text is bound to the entity and moves with it or is displayed at the entity location.

```java
public enum EntityBind {

    NONE,
    LOW_ELEVATION,
    MEDIUM_ELEVATION,
    HIGH_ELEVATION,
}
```

- `NONE`: Text is not bound to the entity and is only placed on its position.
- `LOW_ELEVATION`: Text is bound to the entity and follows her around. The text is slightly above the entity, around 0.5 of a block.
- `LOW_ELEVATION`: Text is bound to the entity and follows her around. The text is slightly more above the entity, around 1 block.
- `LOW_ELEVATION`: Text is bound to the entity and follows her around. The text is above the entity, around 2 blocks.

### Command Sender Type

Whether the command is executed as a player or as a console.

```java
public enum SenderType {

    PLAYER,
    CONSOLE,
}
```

- `PLAYER`: Execute the command as a player.
- `CONSOLE`: Execute the command as a console.

### Command Execution Type

If the command should be executed at the start or the end of the node.

```java
public enum ExecutionType {

    START,
    END,
}
```

- `START`: Execute the command at the start of the node.
- `END`: Execute the command at the end of the node.

### Display Face Type

Defines the orientation of the display relative to the viewer.

```java
public static enum Billboard {

    VERTICAL,
    HORIZONTAL,
    CENTER;
}
```

- `VERTICAL`: Display aligns vertically with the player's view.
- `HORIZONTAL`: Display aligns horizontally with the player's view.
- `CENTER`: Display faces the viewer directly, adjusting orientation as the viewer moves.

### Text Alignment Type

Specifies the horizontal alignment of display text within its designated area.

```java
public static enum TextAlignment {

    CENTER,
    LEFT,
    RIGHT;
}
```

- `CENTER`: Text is centered.
- `LEFT`: Text aligns left.
- `RIGHT`: Text aligns right.

### Camera Target

Represents possible targets for a cutscene camera focus.

```java
public enum CameraTargetType {

    PLAYER,
    ENTITY,
    LOCATION,
}
```

- `PLAYER`: The camera focuses on the player in the conversation.
- `ENTITY`: The camera focuses on the specified entity.
- `LOCATION`: The camera focuses on the specified location.

### Cutscene Type

Type of a cutscene.

```java
public enum CutsceneType {

    NONE,
    CUTSCENE,
    PLAYER,
}
```

- `NONE`: Won't restrict the player and no cutscene is applied.
- `CUTSCENE`: Puts player in a third-person cutscene, focuses his camera, applies zoom, teleports him to specified location and restricts the camera. With the citizens setting, player clone will be created in his place.
- `PLAYER`: Puts player in a first-person cutscene, focuses his camera, applies zoom and restricts his movement.

### Display Target

Where the display text should be created.

```java
public enum DisplayTarget {

    LOCATION,
    ENTITY,
}
```

- `LOCATION`: Display is created at the location.
- `ENTITY`: Display is either bound to the entity and moves with it or is displayed at the entity's location.

## 5. Command Interface

Plugin comes with the `/conversation` command that is used for managing player conversations, handling conversation flow, and managing player-specific requirements and variables. Commands that don’t execute the node will position the player at the node but require a `RUN` command to be executed.

Below is an organized breakdown of each command and its purpose.

### Conversation Management Commands

Commands to initiate, start, reset, restart, and remove players from conversations.

#### Initiate Command

> `Usage:` `/conversation [player] [conversation] initiate`
- Adds player to the conversation.
- Does not execute the node.

#### Start Command

> `Usage:` `/conversation [player] [conversation] start`
- Adds a player to the conversation.
- Does execute the node.

#### Reset Command

> `Usage:` `/conversation [player] [conversation] reset`
- Restarts the conversation for the player.
- Does not execute the node.
- Uses [`REMOVE`](#remove-command) and [`INITIATE`](#initiate-command) command.
- Does not save player-specific requirements and variabless.

#### Restart Command

> `Usage:` `/conversation [player] [conversation] restart`
- Restarts the conversation for the player.
- Does execute the node.
- Uses [`REMOVE`](#remove-command) and [`START`](#start-command) command.
- Does not save player-specific requirements and variables.

#### Remove Command

> `Usage:` `/conversation [player] [conversation] remove`
- Removes a player from the conversation.
- Does not save player-specific requirements and variables.

### Player Management Commands

Commands for setting and managing player-specific requirements and variables.

#### Add Requirement Command

> `Usage:` `/conversation [player] [conversation] add requirement [requirement]`
- Adds a specified requirement for the player within the conversation.

#### Add Variable Command

> `Usage:` `/conversation [player] [conversation] add variable [variable] [value]`
- Adds a specified variable with a given value for the player within the conversation.

#### Clear Requirements Command

> `Usage:` `/conversation [player] [conversation] clear requirements`
- Clears all requirements for the player within the conversation.

#### Clear Variables Command

> `Usage:` `/conversation [player] [conversation] clear variables`
- Clears all variables for the player within the conversation.

#### Remove Requirement Command

> `Usage:` `/conversation [player] [conversation] remove requirement [requirement]`
- Removes a specified requirement for the player within the conversation.

#### Remove Variable Command

> `Usage:` `/conversation [player] [conversation] remove variable [variable]`
- Removes a specified variable for the player within the conversation.

### Debugging Commands

Commands to assist in creating, testing, and troubleshooting conversations.

#### Give Tool Command

> `Usage:` `/conversation give tool`
- Gives the player a conversation tool that provides information on the coordinates of right-clicked blocks or the IDs of right-clicked entities.

#### Print Node Command

> `Usage:` `/conversation [player] [conversation] print`
- Displays the player's current conversation node.

#### Print Requirements Command

> `Usage:` `/conversation [player] [conversation] print requirements`
- Displays all requirements for the player within the conversation.

#### Print Variables Command

> `Usage:` `/conversation [player] [conversation] print variables`
- Displays all variables for the player within the conversation.

### Conversation Flow Commands

Commands to control the progress and flow of players through the conversation nodes.

#### Next Command

> `Usage:` `/conversation [player] [conversation] next`
- Advances player to the next node.
- Does not execute the node.
- Does not ignore node requirements.

#### Continue Command

> `Usage:` `/conversation [player] [conversation] continue`
- Advances player to the next node.
- Does execute the node.
- Does not ignore node requirements.

#### Set commands

> `Usage:` `/conversation [player] [conversation] set [node]`
- Moves the player to a specified node.
- Does not execute the node.
- Ignores node requirements.

> `Usage:` `/conversation [player] [conversation] set next`
- Moves the player to a specified node.
- Does not execute the node.
- Ignores node requirements.

#### Jump Command

> `Usage:` `/conversation [player] [conversation] jump [node]`
- Moves the player to a specified node.
- Does execute the node.
- Ignores node requirements.

> `Usage:` `/conversation [player] [conversation] jump next`
- Moves the player to a specified node.
- Does execute the node.
- Ignores node requirements.

#### Run Command

> `Usage:` `/conversation [player] [conversation] run`
- Executes the current node of the conversation.
- Does execute the node.
