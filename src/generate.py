#this is used to generate resource pack as all the textures need to be combined
from pathlib import Path
import json

root_dir = Path('./assets')

def make_model_file(name,name_space):
    ret = {"parent": "minecraft:item/generated", "textures": {"layer0": f"{name_space}:item/{name}"}}
    write_path = Path(f"./assets/{name_space}/models/item/{name}.json")
    write_path.parent.mkdir(parents=True, exist_ok=True)
    with write_path.open(mode="w") as file:
        json.dump(ret,file)

read_paths = []
names = []
for path in root_dir.iterdir():
    if path.is_dir():
        if path.name == 'minecraft':
            continue
        read_paths.append(Path(f"./{path}/textures/item"))
        names.append(path.name)
x=0
paper_json = {"model": {
    "type": "minecraft:select",
    "property": "minecraft:custom_model_data",
    "cases": [],
    "fallback": {
      "type": "minecraft:model",
      "model": "minecraft:item/paper"
    }
}}
for path in read_paths:
    for item in path.iterdir():
        make_model_file(item.name[:-4],name[x])
        paper_json["model"]["cases"].append({"when": f"{names[x]}:{item.name[:-4]}",
        "model": {
          "type": "minecraft:model",
          "model": f"{name[x]}:item/{item.name[:-4]}"
        }})
    x += 1
minecraft_dir = Path("./assets/minecraft/items/paper.json")
minecraft_dir.parent.mkdir(parents=True, exist_ok=True)
with minecraft_dir.open(mode="w") as file:
    json.dump(paper_json,file)