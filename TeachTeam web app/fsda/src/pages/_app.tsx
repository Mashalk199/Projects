import "@/styles/globals.css";
import { ChakraProvider } from "@chakra-ui/react";
import type { AppProps } from "next/app";
import { AuthProvider } from "@/context/AuthContext";
import { CourseProvider } from "@/context/CourseContext";
import { ApplicationProvider } from "@/context/ApplicationContext";
import Layout from "@/layout/layout";
import { DndProvider } from "react-dnd";
import { HTML5Backend } from "react-dnd-html5-backend";

export default function App({ Component, pageProps }: AppProps) {
  return (
    <AuthProvider>
      <CourseProvider>
        <ApplicationProvider>
          <ChakraProvider>
            <DndProvider backend={HTML5Backend}>
              <Layout>
                <Component {...pageProps} />
              </Layout>
            </DndProvider>
          </ChakraProvider>
        </ApplicationProvider>
      </CourseProvider>
    </AuthProvider>
  );
}
