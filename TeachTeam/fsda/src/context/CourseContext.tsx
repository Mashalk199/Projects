import React, { createContext, useContext, useEffect, useState } from "react";
import { Course, DEFAULT_COURSES } from "@/types/courses";

interface CourseContextType {
  courses: Course[];
}

const CourseContext = createContext<CourseContextType | undefined>(undefined);

export function CourseProvider({ children }: { children: React.ReactNode }) {
  const [courses, setCourses] = useState<Course[]>([]);

  useEffect(() => {
    // Initialize default courses
    localStorage.setItem("courses", JSON.stringify(DEFAULT_COURSES));
    setCourses(DEFAULT_COURSES);
  }, []);
  return (
    <CourseContext.Provider value={{ courses }}>
      {children}
    </CourseContext.Provider>
  );
}
export function useCourses() {
  const context = useContext(CourseContext);
  if (context === undefined) {
    throw new Error("useCourse must be used within a CourseProvider");
  }
  return context;
}
