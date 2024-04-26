"use client";
import { useState } from "react";

export const useInput = (initialValue: string | number) => {
  const [value, setValue] = useState(initialValue);

  const handleChange = (e: any) => {
    setValue(e.target.value);
  };
  return [value, handleChange] as const;
};
