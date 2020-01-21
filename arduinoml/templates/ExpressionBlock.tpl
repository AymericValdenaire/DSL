if ({test} && millis() - time > debounce)
{{
    {expressions}
    time = millis();
}}