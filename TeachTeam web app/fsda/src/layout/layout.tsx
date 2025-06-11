import React, { ReactNode } from "react";
import { Flex } from "@chakra-ui/react";
import Navbar from "@/components/navbar";
import Footer from "@/components/footer";
interface LayoutProps {
  children: ReactNode;
}

const Layout: React.FC<LayoutProps> = ({ children }) => {
  return (
    <Flex direction="column" minHeight="100vh">
      <Navbar />
      {children}
      <Footer />
    </Flex>
  );
};
export default Layout;
