import React, { createContext, useContext, useEffect, useState } from "react";
import { Application, DEFAULT_APPLICATIONS } from "@/types/applications";

interface ApplicationContextType {
  applications: Application[];
  setApplications: React.Dispatch<React.SetStateAction<Application[]>>;
}

const ApplicationContext = createContext<ApplicationContextType | undefined>(
  undefined
);

export function ApplicationProvider({
  children,
}: {
  children: React.ReactNode;
}) {
  const [applications, setApplications] = useState<Application[]>([]);

  useEffect(() => {
    // Initialize default courses
    localStorage.setItem("applications", JSON.stringify(DEFAULT_APPLICATIONS));
    setApplications(DEFAULT_APPLICATIONS);
  }, []);
  return (
    <ApplicationContext.Provider value={{ applications, setApplications }}>
      {children}
    </ApplicationContext.Provider>
  );
}
export function useApplications() {
  const context = useContext(ApplicationContext);
  if (context === undefined) {
    throw new Error(
      "useApplications must be used within a ApplicationProvider"
    );
  }
  return context;
}
