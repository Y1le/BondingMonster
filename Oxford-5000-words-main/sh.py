import json
import os

def json_to_sql(json_file_path, sql_file_path, table_name="ox_words"):
    """
    将JSON文件中的单词数据转换为MySQL INSERT语句。

    Args:
        json_file_path (str): 输入的JSON文件路径。
        sql_file_path (str): 输出的SQL文件路径。
        table_name (str): 要插入数据的MySQL表名。
    """
    sql_statements = []

    # SQL CREATE TABLE 语句 (根据您提供的JSON结构)
    create_table_sql = f"""
CREATE TABLE IF NOT EXISTS `{table_name}` (
    `db_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `word` VARCHAR(255) NOT NULL UNIQUE,
    `href` VARCHAR(500),
    `type` VARCHAR(50),
    `level` VARCHAR(10),
    `us_mp3_url` VARCHAR(500),
    `us_ogg_url` VARCHAR(500),
    `uk_mp3_url` VARCHAR(500),
    `uk_ogg_url` VARCHAR(500),
    `phonetics_us` VARCHAR(50),
    `phonetics_uk` VARCHAR(50),
    `examples_json` TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
"""
    sql_statements.append(create_table_sql)

    try:
        with open(json_file_path, 'r', encoding='utf-8') as f:
            data = json.load(f)

        if not isinstance(data, list):
            print(f"警告: JSON文件 '{json_file_path}' 的根不是一个列表。请确保JSON是一个包含单词对象的数组。")
            return

        for item in data:
            if "value" not in item:
                print(f"警告: 跳过缺少 'value' 字段的条目: {item}")
                continue

            word_data = item["value"]

            word = word_data.get("word", "")
            href = word_data.get("href", "")
            word_type = word_data.get("type", "")
            level = word_data.get("level", "")

            us_mp3_url = word_data.get("us", {}).get("mp3", "")
            us_ogg_url = word_data.get("us", {}).get("ogg", "")
            uk_mp3_url = word_data.get("uk", {}).get("mp3", "")
            uk_ogg_url = word_data.get("uk", {}).get("ogg", "")

            phonetics_us = word_data.get("phonetics", {}).get("us", "")
            phonetics_uk = word_data.get("phonetics", {}).get("uk", "")

            examples = word_data.get("examples", [])
            # 将例句列表转换为JSON字符串
            examples_json = json.dumps(examples, ensure_ascii=False) # ensure_ascii=False 确保中文不被转义

            # 构建INSERT语句
            # 使用 REPLACE INTO 而不是 INSERT INTO 来处理重复的 'word' 字段
            # 如果 'word' 已经存在，它会更新现有行；否则，它会插入新行。
            # 或者使用 INSERT IGNORE INTO 如果你只想跳过重复的行。
            # 这里我使用 INSERT IGNORE INTO，因为通常导入数据时希望跳过已存在的。
            insert_sql = f"""
INSERT IGNORE INTO `{table_name}` (
    `word`, `href`, `type`, `level`,
    `us_mp3_url`, `us_ogg_url`, `uk_mp3_url`, `uk_ogg_url`,
    `phonetics_us`, `phonetics_uk`, `examples_json`
) VALUES (
    '{word.replace("'", "''")}',  -- 转义单引号
    '{href.replace("'", "''")}',
    '{word_type.replace("'", "''")}',
    '{level.replace("'", "''")}',
    '{us_mp3_url.replace("'", "''")}',
    '{us_ogg_url.replace("'", "''")}',
    '{uk_mp3_url.replace("'", "''")}',
    '{uk_ogg_url.replace("'", "''")}',
    '{phonetics_us.replace("'", "''")}',
    '{phonetics_uk.replace("'", "''")}',
    '{examples_json.replace("'", "''")}'
);
"""
            sql_statements.append(insert_sql)

        with open(sql_file_path, 'w', encoding='utf-8') as f:
            for sql in sql_statements:
                f.write(sql.strip() + "\n") # strip() 去除多余的换行符和空格
                if not sql.strip().endswith(';'): # 确保每条语句以分号结束
                    f.write(";\n")
        print(f"成功将数据转换为SQL，并保存到 '{sql_file_path}'")

    except FileNotFoundError:
        print(f"错误: JSON文件 '{json_file_path}' 未找到。")
    except json.JSONDecodeError as e:
        print(f"错误: JSON文件 '{json_file_path}' 解析失败: {e}")
    except Exception as e:
        print(f"发生未知错误: {e}")

if __name__ == "__main__":
    # 假设 full-word.json 和脚本在同一目录下
    json_input_file = "full-word.json"
    sql_output_file = "insert_words.sql"
    target_table_name = "ox_words" # 您的表名

    # 检查JSON文件是否存在
    if not os.path.exists(json_input_file):
        print(f"错误: JSON文件 '{json_input_file}' 不存在。请将其放在脚本同一目录下。")
    else:
        json_to_sql(json_input_file, sql_output_file, target_table_name)