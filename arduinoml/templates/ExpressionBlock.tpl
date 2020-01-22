if ({test} && millis() - time > debounce)
{{
    time = millis();
    {expressions}
}}