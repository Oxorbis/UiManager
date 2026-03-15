import os
import json
import re

def parse_markdown_table(content, header_name):
    # Find the section with the header
    section_pattern = rf"## {header_name}\n\n(.*?)(?=\n## |\Z)"
    match = re.search(section_pattern, content, re.DOTALL)
    if not match:
        return []
    
    table_content = match.group(1).strip()
    lines = table_content.split('\n')
    if len(lines) < 3:
        return []
    
    # Simple table parser
    rows = []
    # Skip header and separator
    for line in lines[2:]:
        if '|' not in line:
            continue
        cols = [c.strip() for c in line.split('|')]
        # Filter empty strings from split
        cols = [c for c in cols if c]
        if cols:
            rows.append(cols)
    return rows

def clean_type(type_str):
    # Remove markdown links [Type](../path) -> Type
    type_str = re.sub(r'\[(.*?)\]\(.*?\)', r'\1', type_str)
    # Split by '/' or ',' for multiple types
    types = [t.strip() for t in re.split(r'[/,]', type_str)]
    return types

def clean_name(name_str):
    # Remove bold markdown **Name** -> Name
    return name_str.replace('**', '').strip()

def process_docs(base_path):
    report = {
        "nodes": {},
        "types": {},
        "enums": {}
    }

    # Process Enums
    enums_path = os.path.join(base_path, 'enums')
    if os.path.exists(enums_path):
        for filename in os.listdir(enums_path):
            if filename.endswith('.md') and filename != 'README.md':
                enum_name = filename[:-3].capitalize()
                # Try to get better name from title
                with open(os.path.join(enums_path, filename), 'r') as f:
                    content = f.read()
                    title_match = re.search(r'^title: (.*)$', content, re.MULTILINE)
                    if title_match:
                        enum_name = title_match.group(1).strip()
                    
                    values_table = parse_markdown_table(content, "Values")
                    values = [clean_name(row[0]) for row in values_table if row]
                    report["enums"][enum_name] = values

    # Process Property Types
    types_path = os.path.join(base_path, 'property-types')
    if os.path.exists(types_path):
        for filename in os.listdir(types_path):
            if filename.endswith('.md') and filename != 'README.md':
                type_name = filename[:-3].capitalize()
                with open(os.path.join(types_path, filename), 'r') as f:
                    content = f.read()
                    title_match = re.search(r'^title: (.*)$', content, re.MULTILINE)
                    if title_match:
                        type_name = title_match.group(1).strip()
                    
                    props_table = parse_markdown_table(content, "Properties")
                    properties = {}
                    for row in props_table:
                        if len(row) >= 2:
                            name = clean_name(row[0])
                            types = clean_type(row[1])
                            properties[name] = types
                    
                    report["types"][type_name] = {
                        "name": type_name,
                        "properties": properties
                    }

    # Process Elements (Nodes)
    elements_path = os.path.join(base_path, 'elements')
    if os.path.exists(elements_path):
        for filename in os.listdir(elements_path):
            if filename.endswith('.md') and filename != 'README.md':
                node_name = filename[:-3].capitalize()
                with open(os.path.join(elements_path, filename), 'r') as f:
                    content = f.read()
                    title_match = re.search(r'^title: (.*)$', content, re.MULTILINE)
                    if title_match:
                        node_name = title_match.group(1).strip()
                    
                    # Properties
                    props_table = parse_markdown_table(content, "Properties")
                    properties = {}
                    for row in props_table:
                        if len(row) >= 2:
                            name = clean_name(row[0])
                            types = clean_type(row[1])
                            properties[name] = types
                    
                    # Events
                    events_table = parse_markdown_table(content, "Event Callbacks")
                    events = [clean_name(row[0]) for row in events_table if row]
                    
                    # Children
                    accepts_children = "Accepts child elements: Yes" in content
                    
                    report["nodes"][node_name] = {
                        "name": node_name,
                        "properties": properties,
                        "events": events,
                        "children": [] # Placeholder, as we don't have list of specific allowed children in md usually
                    }

    return report

if __name__ == "__main__":
    docs_base = "uiManager/src/main/resources/docs/custom-ui/type-documentation"
    output_file = "uiManager/ui_structure_report_new.json"
    
    # If script is run from project root
    if not os.path.exists(docs_base):
        print(f"Error: {docs_base} not found. Run from project root.")
    else:
        result = process_docs(docs_base)
        with open(output_file, 'w') as f:
            json.dump(result, f, indent=2)
        print(f"Successfully generated {output_file}")
