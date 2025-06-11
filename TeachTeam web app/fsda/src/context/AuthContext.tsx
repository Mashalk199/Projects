import React, { createContext, useContext, useEffect, useState } from "react";
import { Tutor, DEFAULT_TUTORS } from "../types/tutors";
import { Lecturer, DEFAULT_LECTURERS } from "@/types/lecturers";

interface AuthContextType {
  user: Tutor | Lecturer | null;
  tutors: Tutor[];
  lecturers: Lecturer[];
  login: (username: string, password: string) => boolean;
  logout: () => void;
  setTutors: React.Dispatch<React.SetStateAction<Tutor[]>>;
  setLecturers: React.Dispatch<React.SetStateAction<Lecturer[]>>;
  setUser: React.Dispatch<React.SetStateAction<Tutor | Lecturer | null>>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<Tutor | Lecturer | null>(null);
  const [tutors, setTutors] = useState<Tutor[]>([]);
  const [lecturers, setLecturers] = useState<Lecturer[]>([]);

  useEffect(() => {
    // Initialize default users
    const storedTutors = localStorage.getItem("tutors");
    if (!storedTutors) {
      setTutors(DEFAULT_TUTORS);
      localStorage.setItem("tutors", JSON.stringify(DEFAULT_TUTORS));
    } else {
      setTutors(JSON.parse(storedTutors));
    }
    const storedLecturers = localStorage.getItem("lecturers");
    if (!storedLecturers) {
      setLecturers(DEFAULT_LECTURERS);
      localStorage.setItem("lecturers", JSON.stringify(DEFAULT_LECTURERS));
    } else {
      setLecturers(JSON.parse(storedLecturers));
    }

    // Check for existing login
    const storedUser = localStorage.getItem("currentUser");
    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }
  }, []);
  const login = (username: string, password: string): boolean => {
    const foundTutor = tutors.find(
      (u) => u.username === username && u.password === password
    );

    if (foundTutor) {
      setUser(foundTutor);
      localStorage.setItem("currentUser", JSON.stringify(foundTutor));
      return true;
    } else {
      const foundLecturer = lecturers.find(
        (u) => u.username === username && u.password === password
      );
      if (foundLecturer) {
        setUser(foundLecturer);
        localStorage.setItem("currentUser", JSON.stringify(foundLecturer));
        return true;
      }
    }
    return false;
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem("currentUser");
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        tutors,
        lecturers,
        login,
        logout,
        setTutors,
        setLecturers,
        setUser,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
}
